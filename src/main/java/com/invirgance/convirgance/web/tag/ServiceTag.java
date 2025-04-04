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
    
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public int doStartTag() throws JspException
    {
        this.parameters = new JSONObject();
        
        return EVAL_BODY_INCLUDE;
    }

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

    @Override
    public Object get(String key)
    {
        return this.parameters.get(key);
    }

    @Override
    public void set(String key, Object value)
    {
        this.parameters.put(key, value);
    }
}
