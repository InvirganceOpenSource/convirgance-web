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

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.servlet.ServiceCaller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyTagSupport;

/**
 * A custom JSP tag that calls a service endpoint and stores the results.
 * This tag invokes a service specified by a path and provides parameters to that
 * service using key-value pairs. As an implementation of {@link KeyValueTypeTag}, 
 * it allows parameters to be added through nested {@link KeyTag} elements.
 * 
 * @author jbanes
 */
public class ServiceTag extends BodyTagSupport implements KeyValueTypeTag
{
    private String variable;
    private String scope = "page";
    private String path;
    
    private JSONObject parameters;

    
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
    
    /**
     * Gets the variable name this is assigned to.
     * 
     * @return The name.
     */
    public String getVar()
    {
        return variable;
    }

    /**
     * Sets the variable this is assigned to.
     * 
     * @param variable The name.
     */
    public void setVar(String variable)
    {
        this.variable = variable;
    }

    /**
     * Gets the scope this is accessible in.
     * 
     * @return The scope.
     */    
    public String getScope()
    {
        return scope;
    }
    
    /**
     * Sets the scope this can be accessed from.
     * 
     * @param scope The scope.
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }
    
    /**
     * Gets the service path that will be called.
     * 
     * @return The service path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path for the service endpoint to call.
     * 
     * @param path The service path
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * Initializes the tag by creating a new parameter object.
     * 
     * @return EVAL_BODY_INCLUDE to process the tag body
     * @throws JspException If initialization fails
     */
    @Override
    public int doStartTag() throws JspException
    {
        this.parameters = new JSONObject();
        
        return EVAL_BODY_INCLUDE;
    }
    
    /**
     * Calls the service endpoint with the collected parameters and 
     * stores the results in the specified variable.
     * 
     * @return EVAL_PAGE to continue processing the page
     * @throws JspException If the service call fails
     */
    @Override
    public int doEndTag() throws JspException
    {
        Iterable<JSONObject> results = ServiceCaller.select((HttpServletRequest)pageContext.getRequest(), path, parameters);
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, results, getScopeInt());
        }
        
        this.parameters = null;
        this.path = null;
        this.scope = "page";
        this.variable = null;
        
        return EVAL_PAGE;
    }
    
    /**
     * Gets a parameter value for the specified key.
     * Implements the KeyValueTypeTag interface.
     * 
     * @param key The parameter name
     * @return The parameter value
     */
    @Override
    public Object get(String key)
    {
        return this.parameters.get(key);
    }
    
    /**
     * Sets a parameter value for the specified key.
     * These parameters are passed to the service endpoint.
     * 
     * @param key The parameter name
     * @param value The parameter value
     */
    @Override
    public void set(String key, Object value)
    {
        this.parameters.put(key, value);
    }
}
