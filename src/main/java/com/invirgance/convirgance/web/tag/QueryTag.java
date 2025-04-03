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
package com.invirgance.convirgance.web.tag;

import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.servlet.ApplicationInitializer;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import static jakarta.servlet.jsp.tagext.Tag.SKIP_BODY;
import javax.sql.DataSource;

/**
 *
 * @author jbanes
 */
public class QueryTag extends BodyTagSupport
{
    private String variable;
    private String jndi;
    private String scope = "page";
    private JSONObject binding;

    private int getScopeInt() throws JspException
    {
        switch(scope)
        {
            case "page": return PageContext.PAGE_SCOPE;
            case "request": return PageContext.REQUEST_SCOPE;
            case "session": return PageContext.SESSION_SCOPE;
            case "application": return PageContext.APPLICATION_SCOPE;
        }
        
        throw new JspException("Invalid scope: " + scope);
    }
    
    public String getVar()
    {
        return variable;
    }

    public void setVar(String variable)
    {
        this.variable = variable;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getJndi()
    {
        return jndi;
    }

    public void setJndi(String jndi)
    {
        this.jndi = jndi;
    }

    public JSONObject getBinding()
    {
        return binding;
    }

    public void setBinding(JSONObject binding)
    {
        this.binding = binding;
    }

    private DBMS lookup()
    {
        DataSource source = ApplicationInitializer.lookup(this.jndi);
        
        if(source == null) return DBMS.lookup(jndi);
        
        return new DBMS(source);
    }
    
    @Override
    public int doAfterBody() throws JspException
    {
        DBMS dbms = lookup();
        Query query = new Query(bodyContent.getString());

        if(this.binding != null)
        {
            query.setBindings(binding);
        }
        else
        {
            for(String name : query.getParameterNames())
            {
                query.setBinding(name, pageContext.findAttribute(name));
            }
        }
        
        pageContext.setAttribute(this.variable, dbms.query(query), getScopeInt());

        return SKIP_BODY;
    }
    
    
}
