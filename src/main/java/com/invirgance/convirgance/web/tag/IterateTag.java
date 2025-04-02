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

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import java.util.Iterator;

/**
 *
 * @author jbanes
 */
public class IterateTag extends BodyTagSupport
{
    private String variable;
    private String scope = "page";
    private Iterable items;
    private int limit = 0;
    private int skip = 0;
    
    private int index;
    private Object item;
    private Iterator iterator;
    
    
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

    public Iterable getItems()
    {
        return items;
    }

    public void setItems(Iterable items)
    {
        this.items = items;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getSkip()
    {
        return skip;
    }

    public void setSkip(int skip)
    {
        this.skip = skip;
    }

    @Override
    public int doStartTag() throws JspException
    {
        iterator = items.iterator();
        index = 0;
        
        if(!iterator.hasNext()) return SKIP_BODY;
        
        item = iterator.next();
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, item, getScopeInt());
        }
        
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException
    {
        if(!iterator.hasNext()) return SKIP_BODY;
        
        item = iterator.next();
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, item, getScopeInt());
        }
        
        index++;
        
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException
    {
        iterator = null;
        item = null;
        variable = null;
        scope = "page";
        limit = 0;
        skip = 0;
        
        return EVAL_PAGE;
    }
}
