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
import com.invirgance.convirgance.json.JSONObject;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

/**
 *
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
    
    public Object getValue()
    {
        return this.array;
    }
    
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
