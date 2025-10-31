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
package com.invirgance.convirgance.web.service;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.TransactionOperation;
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.parameter.Parameter;
import com.invirgance.convirgance.web.servlet.ApplicationInitializer;
import com.invirgance.convirgance.web.servlet.ServiceState;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Convenience service for executing simple SQL updates like delete or update. Provides
 * the ability to run multiple queries as a single transaction. If any of the queries
 * fails, the entire transaction will be rolled back.
 * <br>
 * For convenience, the {@link #setSql(String)} method is provided to configure a single
 * query. This is the same as calling {@link #setStatements(List)} with a single list
 * item.
 * @author jbanes
 */
@Wiring
public class UpdateService implements Service, Processable
{
    private List<Parameter> parameters;
    private String jndiName;
    private List<String> statements;

    /**
     * Gets the list of parameters to extract from the request.
     *
     * @return The parameters list
     */
    public List<Parameter> getParameters()
    {
        return parameters;
    }
    
    /**
     * Sets the list of parameters to extract from the request.
     *
     * @param parameters The parameters list
     */
    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }
    
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
     * Returns a list of SQL queries that will be executed.
     * 
     * @return A list of SQL queries
     */
    public List<String> getStatements()
    {
        return statements;
    }

    /**
     * Sets a list of queries used to update the database. Named bindings can be 
     * used. They will be bound by key to the parameters provided by the 
     * parameters.
     * 
     * @param sql The new SQL query.
     */
    public void setStatements(List<String> sql)
    {
        this.statements = sql;
    }
    
    /**
     * Returns a single SQL query to execute.
     * 
     * @return The SQL query.
     */
    public String getSql()
    {
        if(this.statements == null || this.statements.size() != 1) return null;
        
        return this.statements.get(0);
    }
    
    /**
     * Sets a single query used to update the database. Named bindings can be 
     * used. They will be bound by key to the parameters provided by the 
     * parameters.
     * 
     * @param sql The new SQL query.
     */
    public void setSql(String sql)
    {
        this.statements = new ArrayList<>();
        
        this.statements.add(sql);
    }
    
    private DBMS lookup()
    {
        DataSource source = ApplicationInitializer.lookup(this.jndiName);
        
        if(jndiName == null) throw new ConvirganceException("jndiName property must be set!");
        if(source == null) return DBMS.lookup(jndiName);
        
        return new DBMS(source);
    }

    @Override
    public Iterable<JSONObject> process(HttpRequest request)
    {
        var dbms = lookup();
        var params = new JSONObject();
        var transaction = new TransactionOperation();
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        
        // Obtain the parameters for binding
        for(Parameter parameter : this.parameters)
        {
            params.put(parameter.getName(), parameter.getValue(request));
        }
        
        // Record the bindings to a thread local so it can be referenced deep in the heirarchy
        ServiceState.set("parameters", params);
        
        // Bind the parameters and build a transaction
        for(var statement : this.statements)
        {
            transaction.add(new Query(statement, params));
        }
        
        // Execute the transaction
        dbms.update(transaction);
        
        // No data to return
        return new JSONArray<>();
    }
    
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        process(request);
    }
    
}
