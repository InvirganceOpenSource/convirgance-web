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
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

/**
 * A custom JSP tag that creates and manages a JSON object.This tag creates
 * a {@link JSONObject} that can be populated with key-value pairs using 
 * nested {@link KeyTag} elements. 
 * 
 * <pre>
 * The resulting object can be:
 * - Stored in a page-scoped variable using the {@code var} attribute
 * - Passed to a parent tag that implements {@link ValueTypeTag}
 * </pre>
 * 
 * @author jbanes
 */
public class ObjectTag extends TagSupport implements KeyValueTypeTag
{
    private String variable;
    private String scope = "page";
    private JSONObject object;
    
    private Tag parent;

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
    
    private ValueTypeTag findParent()
    {
        Tag parent = getParent();
        
        while(parent != null)
        {
            if(parent instanceof ValueTypeTag) return (ValueTypeTag)parent;
            
            parent = parent.getParent();
        }
        
        return null;
    }
    
    /**
     * Gets the variable this is assigned to.
     * 
     * @return The variable name.
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

    @Override
    public Tag getParent()
    {
        return this.parent;
    }

    @Override
    public void setParent(Tag parent)
    {
        this.parent = parent;
    }
    
    /**
     * Gets the value for the specified key from the JSON object.
     * 
     * @param key The key name.
     * @return The value associated with the key.
     */
    public Object get(String key)
    {
        return this.object.get(key);
    }
    
    /**
     * Sets a value for the specified key in the JSON object.
     * 
     * @param key The key name.
     * @param value The value to associate with the key.
     */
    public void set(String key, Object value)
    {
        this.object.put(key, value);
    }

    @Override
    public int doStartTag() throws JspException
    {
        this.object = new JSONObject(true);
        
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException
    {
        ValueTypeTag parent = findParent();
        
        if(parent != null)
        {
            parent.setValue(object);
        }
        
        if(variable != null) 
        {
            pageContext.setAttribute(this.variable, object, getScopeInt());
        }
        
        this.object = null;
        this.variable = null;
        
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        this.object = null;
        this.variable = null;
        
        super.release();
    }
}
