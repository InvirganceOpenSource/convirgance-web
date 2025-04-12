/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.web.binding;

import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.servlet.ApplicationInitializer;
import javax.sql.DataSource;

/**
 * For retrieving data from SQL database.
 * QueryBinding executes parameterized SQL queries against a database and
 * returns the results as JSON objects. It supports named parameter binding, 
 * allowing request parameters to be safely used in SQL statements. 
 * 
 * @author jbanes
 */
public class QueryBinding implements Binding
{
    private String jndiName;
    private String sql;

    /**
     * The JNDI name used to look up the database connection.
     * 
     * <p>This property must reference a valid JNDI resource that returns a
     * {@link DataSource}.</p>
     *
     * @return the JNDI name
    */
    public String getJndiName()
    {
        return jndiName;
    }

    /**
     * Sets the JNDI name for the data source lookup.
     * 
     * @param jndiName The JDNI name.
     */
    public void setJndiName(String jndiName)
    {
        this.jndiName = jndiName;
    }

    /**
     * Returns the SQL query that will be executed.
     * 
     * @return The SQL query.
     */
    public String getSql()
    {
        return sql;
    }

    /**
     * Sets the query used to retrieve data. 
     * Named bindings can be used, they will be bound by key, to the 
     * parameters provided through through {@link #getBinding(JSONObject)}.
     * 
     * @param sql The new SQL query.
     */
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    private DBMS lookup()
    {
        DataSource source = ApplicationInitializer.lookup(this.jndiName);
        
        if(source == null) return DBMS.lookup(jndiName);
        
        return new DBMS(source);
    }

    /**
     * Executes the SQL query with the provided parameters.
     * Parameter values are bound to named parameters in the SQL query.
     * 
     * @param parameters JSON object containing parameter names and values
     * @return Query results as an iterable of JSONObjects
     */
    @Override
    public Iterable<JSONObject> getBinding(JSONObject parameters)
    {
        DBMS dbms = lookup();
        Query query = new Query(sql, parameters);
        
        return dbms.query(query);
    }
}
