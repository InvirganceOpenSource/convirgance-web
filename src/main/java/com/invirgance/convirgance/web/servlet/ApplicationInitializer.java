package com.invirgance.convirgance.web.servlet;


import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.jdbc.datasource.DriverDataSource;
import com.invirgance.convirgance.source.ClasspathSource;
import javax.sql.DataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import javax.naming.*;

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

/**
 * Web application lifecycle listener that will load an <code>application.properties</code>
 * file if included in the project.
 *
 * @author jbanes
 */
public class ApplicationInitializer implements ServletContextListener
{
    private static HashMap<String,DataSource> jndiLookup = new HashMap<>();
    
    public static DataSource lookup(String path)
    {
        return jndiLookup.get(path);
    }
    
    private Properties getProperties()
    {
        Properties props = new Properties();
        
        try
        {
            props.load(new ClasspathSource("/application.properties").getInputStream());
        }
        catch(IOException e) { throw new ConvirganceException(e); }
        
        return props;
    }
    
    private DataSource getDataSource(Properties props)
    {
        String url = props.getProperty("jdbc.database.url");
        String username = props.getProperty("jdbc.database.username", "");
        String password = props.getProperty("jdbc.database.password", "");
        
        try
        {
            Class.forName("com.invirgance.convirgance.jdbc.datasource.DriverDataSource");
        }
        catch(Exception e)
        {
            throw new ConvirganceException("You must add convirgance-jdbc as a dependency to this project if you wish to use application.properties", e);
        }
        
        return getDataSource(url, username, password);
    }

    private DataSource getDataSource(String url, String username, String password)
    {
        return DriverDataSource.getDataSource(url, username, password);
    }
    
    private void registerDataSource(DataSource source, String path)
    {
        try
        {
            jndiLookup.put(path, source);
            
            new InitialContext().rebind(path, source);
        }
        catch(NamingException naming) 
        {
            System.out.println("Unable to bind to " + path + ". JNDI lookup will not be available...");
        }
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        Properties props = getProperties();
        DataSource source = getDataSource(props);
        
        System.out.println("Intializing Convirgance Web Services Application...");
        
        if(props.containsKey("jdbc.database.jndi")) 
        {
            registerDataSource(source, props.getProperty("jdbc.database.jndi"));
        }

        if(props.containsKey("jdbc.init.sql.schema"))
        {
            System.out.println("Intializing database schema from file [" + props.getProperty("jdbc.init.sql.schema") + "]...");
            
            new DBMS(source).update(new Query(new ClasspathSource(props.getProperty("jdbc.init.sql.schema"))));
        }

        if(props.containsKey("jdbc.init.sql.data"))
        {
            System.out.println("Intializing database data from file [" + props.getProperty("jdbc.init.sql.schema") + "]...");
            
            new DBMS(source).update(new Query(new ClasspathSource(props.getProperty("jdbc.init.sql.data"))));
        }
        
        System.out.println("Convirgance Web Services Application initialization complete");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }
}
