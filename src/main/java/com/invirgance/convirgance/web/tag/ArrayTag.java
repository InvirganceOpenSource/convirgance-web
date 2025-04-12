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

import com.invirgance.convirgance.json.JSONArray;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

/**
 * A JSP tag that creates and manages a JSON array.
 * This tag creates a {@link JSONArray} object that can be populated with values
 * by nested tags such as {@link ValueTag}. 
 * 
 * <pre>
 * The resulting array can be:
 * - Stored in a page-scoped variable using the {@code var} attribute
 * - Passed to a parent tag that implements {@link ValueTypeTag}
 * </pre>
 * @author jbanes
 */
public class ArrayTag extends TagSupport implements ValueTypeTag
{
    private String variable;
    private String scope = "page";
    private JSONArray array;
    
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
     * The name of the variable where this will be stored.
     * 
     * @return The variable.
     */
    public String getVar()
    {
        return variable;
    }

    /**
     * Sets the variable for where the JSONArray will be stored.
     * 
     * @param variable The variable name
     */
    public void setVar(String variable)
    {
        this.variable = variable;
    }

    /**
     * Gets the scope for where this will be stored.
     * 
     * @return The scope
     */
    public String getScope()
    {
        return scope;
    }

    /**
     * Sets the scope for where this will be stored.
     * This changes its access level.
     * <pre>
     * Valid options are:
     * - page
     * - request
     * - session
     * - application
     * </pre>
     * 
     * @param scope The scope location.
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }
    
    /**
     * Returns JSON array managed by this tag.
     * 
     * @return The array.
     */
    public Object getValue()
    {
        return this.array;
    }
    
    /**
     * Adds a value to the array. Called by child tags to contribute values.
     * 
     * @param value The value to add.
     */
    public void setValue(Object value)
    {
        this.array.add(value);
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

    @Override
    public int doStartTag() throws JspException
    {
        this.array = new JSONArray();
        
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException
    {
        ValueTypeTag parent = findParent();
        
        if(parent != null)
        {
            parent.setValue(array);
        }
        
        if(variable != null) 
        {
            pageContext.setAttribute(this.variable, array, getScopeInt());
        }
        
        this.variable = null;
        this.array = null;
        
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        this.variable = null;
        this.array = null;
        
        super.release();
    }
}
