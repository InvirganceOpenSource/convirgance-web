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
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.IdentityTransformer;
import com.invirgance.convirgance.web.servlet.ApplicationInitializer;
import javax.sql.DataSource;

/**
 *
 * @author jbanes
 */
public class QueryConsumer implements Consumer
{
    private String jndiName;
    private String sql;
    
    private Query sequenceSql;
    private String sequenceId;
    
    private String cachedKey;

    public String getJndiName()
    {
        return jndiName;
    }

    public void setJndiName(String jndiName)
    {
        this.jndiName = jndiName;
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }

    public String getSequenceSql()
    {
        return sequenceSql.getSQL();
    }

    public void setSequenceSql(String sequenceSql)
    {
        this.sequenceSql = new Query(sequenceSql);
    }

    public String getSequenceId()
    {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId)
    {
        this.sequenceId = sequenceId;
    }
    
    private String getKey(JSONObject next)
    {
        if(this.cachedKey == null) this.cachedKey = (String)next.keySet().toArray()[0];
        
        return this.cachedKey;
    }
    
    public AtomicOperation getOperation(Iterable<JSONObject> iterable, DBMS dbms, JSONArray keys)
    {
        Query query = new Query(sql);
        
        if(sequenceSql != null)
        {
            if(sequenceId == null) throw new ConvirganceException("Property sequenceId must be set when using sequenceSql");
            
            iterable = new IdentityTransformer() {
                @Override
                public JSONObject transform(JSONObject record) throws ConvirganceException
                {
                    try(var result = (CloseableIterator<JSONObject>)dbms.query(sequenceSql).iterator())
                    {
                        JSONObject next = result.next();
                        String key = getKey(next);
                        Object value = next.get(key);
                        
                        record.put(sequenceId, value);
                        keys.add(value);
                        
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
    
    @Override
    public JSONArray consume(Iterable<JSONObject> iterable, JSONObject parameters)
    {
        DBMS dbms = lookup();
        JSONArray keys = new JSONArray();
        
        dbms.update(getOperation(iterable, dbms, keys));
        
        return keys;
    }
    
}
