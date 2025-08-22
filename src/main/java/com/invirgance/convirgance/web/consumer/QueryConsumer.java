/*
 * The MIT License
 *
 * Copyright 2025 jbanes.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.invirgance.convirgance.web.consumer;

import com.invirgance.convirgance.CloseableIterator;
import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.dbms.AtomicOperation;
import com.invirgance.convirgance.dbms.BatchOperation;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.TransactionOperation;
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.IdentityTransformer;
import com.invirgance.convirgance.web.servlet.ApplicationInitializer;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Inserts stream of data into a SQL database. Supports the use of sequences to
 * allow for inserts of children in the same flow. Each object is bound to a
 * batch SQL operation with named parameters.
 * 
 * @author jbanes
 */
@Wiring
public class QueryConsumer implements Consumer
{
    private String jndiName;
    private String sql;
    
    private Query sequenceSql;
    private String sequenceId;
    
    private Map<String,QueryConsumer> children;
    
    private String cachedKey;
    
    //TODO: Allow failure on no records
    //private boolean required;

    /**
     * Returns the JDNI name.
     * 
     * @return The name.
     */
    public String getJndiName()
    {
        return jndiName;
    }

    /**
     * Updates the JDNI connection name to use.
     * 
     * @param jndiName The new connection.
     */
    public void setJndiName(String jndiName)
    {
        this.jndiName = jndiName;
    }

    /**
     * Returns the SQL that will be executed.
     * 
     * @return The raw unbound SQL.
     */
    public String getSql()
    {
        return sql;
    }

    /**
     * Sets the query to execute.
     * Named bindings are supported.
     * 
     * @param sql The query.
     */
    public void setSql(String sql)
    {
        this.sql = sql;
    }

    /**
     * Returns query used to get the next sequence id.
     * 
     * @return The query.
     */
    public String getSequenceSql()
    {
        return sequenceSql.getSQL();
    }

    /**
     * Sets the query used to get the next sequence id.
     * 
     * @param sequenceSql The query.
     */
    public void setSequenceSql(String sequenceSql)
    {
        this.sequenceSql = new Query(sequenceSql);
    }

    /**
     * The field name.
     * 
     * @return The name.
     */
    public String getSequenceId()
    {
        return sequenceId;
    }

    /**
     * Sets the field name to store sequence ids.
     * 
     * @param sequenceId Field name.
     */
    public void setSequenceId(String sequenceId)
    {
        this.sequenceId = sequenceId;
    }

    /**
     * Child records to include as part of the insertion. The key must refer to
     * a JSONArray in the incoming data. The specified QueryConsumer will handle
     * the records found in the JSONArray.
     * 
     * @return The mapping of keys to consumers of child records
     */
    public Map<String,QueryConsumer> getChildren()
    {
        return children;
    }

    /**
     * Child records to include as part of the insertion. The key must refer to
     * a JSONArray in the incoming data. The specified QueryConsumer will handle
     * the records found in the JSONArray.
     * 
     * @param children The mapping of keys to consumers of child records
     */
    public void setChildren(Map<String,QueryConsumer> children)
    {
        this.children = children;
    }
    
    private String getKey(JSONObject next)
    {
        if(this.cachedKey == null) this.cachedKey = (String)next.keySet().toArray()[0];
        
        return this.cachedKey;
    }
    
    /**
     * Creates an {@link AtomicOperation} for database insertion, optionally
     * generating sequence values.
     * <p>
     * If sequence generation is configured, this method will fetch sequence values 
     * before building the batch operation.
     *
     * @param iterable Data that will be transformed
     * @param dbms The DBMS to get the sequence ids.
     * @param keys Output collection where generated sequence values will be stored
     * @return An {@link BatchOperation} with the transformed data to add to the database.
     * @throws ConvirganceException if sequenceSql is set but sequenceId is not
     */
    public AtomicOperation getOperation(Iterable<JSONObject> iterable, DBMS dbms, JSONArray<JSONObject> keys)
    {
        Query query = new Query(sql);
        JSONArray<JSONObject> records = new JSONArray<>();
        TransactionOperation transaction = new TransactionOperation();
        
        Iterable<JSONObject> child;
        
        if(children != null && children.size() > 0)
        {
            transaction.add(new BatchOperation(query, records));
            
            for(JSONObject record : iterable)
            {
                JSONObject keyRecord = new JSONObject();
                
                records.add(record);
                
                if(sequenceId != null)
                {
                    try(var result = (CloseableIterator<JSONObject>)dbms.query(sequenceSql).iterator())
                    {
                        JSONObject next = result.next();
                        String key = getKey(next);
                        Object value = next.get(key);
                        
                        record.put(sequenceId, value);
                        keyRecord.put(sequenceId, value);
                        keys.add(keyRecord);
                    }
                    catch(Exception e) { throw new ConvirganceException(e); }
                }
                
                for(String key : children.keySet())
                {
                    JSONArray childKeys = new JSONArray();
                    child = record.getJSONArray(key);
                    
                    if(sequenceId != null)
                    {
                        Object value = record.get(sequenceId);
                        child = new IdentityTransformer() {
                            @Override
                            public JSONObject transform(JSONObject record) throws ConvirganceException
                            {
                                record.put(sequenceId, value);
                                
                                return record;
                            }
                        }.transform(child);
                    }
                    
                    transaction.add(children.get(key).getOperation(child, dbms, childKeys));
                    keyRecord.put(key, childKeys);
                }
            }
            
            return transaction;
        }

        
        if(sequenceSql != null)
        {
            if(sequenceId == null) throw new ConvirganceException("Property sequenceId must be set when using sequenceSql");
            
            iterable = new IdentityTransformer() {
                @Override
                public JSONObject transform(JSONObject record) throws ConvirganceException
                {
                    try(var result = (CloseableIterator<JSONObject>)dbms.query(sequenceSql).iterator())
                    {
                        JSONObject returnRecord = new JSONObject();
                        JSONObject next = result.next();
                        String key = getKey(next);
                        Object value = next.get(key);
                        
                        record.put(sequenceId, value);
                        returnRecord.put(sequenceId, value);
                        keys.add(returnRecord);
                        
                        return record;
                    }
                    catch(Exception e) { throw new ConvirganceException(e); }
                }
            }.transform(iterable);
        }
        
        return new BatchOperation(query, iterable);
    }
            
    private DBMS lookup()
    {
        DataSource source = ApplicationInitializer.lookup(this.jndiName);
        
        if(source == null) return DBMS.lookup(jndiName);
        
        return new DBMS(source);
    }
    
    /**
     * Consumes a collection of JSONObject records, inserting them into the
     * connected database.
     *
     * @param iterable Collection of JSONObject records to be inserted into the
     * database
     * @param parameters Additional parameters that might influence the
     * operation (unused in this implementation)
     * @return A JSONArray containing any generated sequence values, or an empty
     * array if no sequences were used
     * @throws ConvirganceException if database errors occur or if sequenceSql
     * is set without sequenceId
     */    
    @Override
    public Iterable<JSONObject> consume(Iterable<JSONObject> iterable, JSONObject parameters)
    {
        DBMS dbms = lookup();
        JSONArray<JSONObject> keys = new JSONArray<>();
        
        dbms.update(getOperation(iterable, dbms, keys));
        
        return keys;
    }
    
}
