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
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import java.util.Iterator;

/**
 * A custom JSP tag that iterates over items in a collection.
 * This tag processes each item in a provided {@link Iterable} collection and 
 * makes the current item available as a page variable. Supports pagination 
 * through skip and limit attributes and can provide status information about 
 * the current iteration.
 * 
 * @author jbanes
 */
public class IterateTag extends BodyTagSupport
{
    private String variable;
    private String scope = "page";
    private String status;
    private Iterable items;
    private int limit = 0;
    private int skip = 0;
    
    private int index;
    private int count;
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
    
    private JSONObject getLoopStatus()
    {
        JSONObject status = new JSONObject();
        
        status.put("count", count);
        status.put("index", index);
        status.put("first", (index == 0));
        status.put("last", !iterator.hasNext());
        
        return status;
    }
    
    /**
     * Gets the variable where this will be stored.
     * 
     * @return The variable
     */
    public String getVar()
    {
        return variable;
    }

    /**
     * Sets the variable that will store this.
     * @param variable The variable name.
     */
    public void setVar(String variable)
    {
        this.variable = variable;
    }

    /**
     * Gets the current access scope.
     * 
     * @return Returns the scope this is accessible in.
     */
    public String getScope()
    {
        return scope;
    }

    /**
     * Sets the scope where this will be accessible.
     *<pre>
     * Valid options are:
     * - page
     * - request
     * - session
     * - application
     * </pre>
     * 
     * @param scope 
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }

    /**
     * Returns the variable name where iteration status information is stored.
     * Status includes index, count, first, and last properties.
     * 
     * @return The status variable.
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * Sets the variable name where loop status information will be stored.
     * When provided, each iteration will update this variable with current
     * loop status (count, index, first, last).
     * 
     * @param status The status variable name.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Returns the items.
     * 
     * @return The items.
     */
    public Iterable getItems()
    {
        return items;
    }

    /**
     * Sets the items that will be used by the tag.
     * 
     * @param items An iterable.
     */
    public void setItems(Iterable items)
    {
        this.items = items;
    }

    /**
     * Gets the limit of the iterator.
     * 
     * @return The limit.
     */
    public int getLimit()
    {
        return limit;
    }

    /**
     * Sets the item limit.
     * Can be used for pagination.
     * 
     * @param limit The limit.
     */
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    /**
     * Returns the item skip amount.
     * 
     * @return The items skip amount.
     */
    public int getSkip()
    {
        return skip;
    }

    /**
     * Sets the amount of items to skip.
     * 
     * @param skip The amount.
     */
    public void setSkip(int skip)
    {
        this.skip = skip;
    }

    @Override
    public int doStartTag() throws JspException
    {
        iterator = items.iterator();
        index = 0;
        count = 0;
        
        while(index < skip && iterator.hasNext())
        {
            iterator.next();
            index++;
        }
        
        if(!iterator.hasNext()) return SKIP_BODY;
        
        item = iterator.next();
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, item, getScopeInt());
        }
        
        if(status != null)
        {
            pageContext.setAttribute(status, getLoopStatus());
        }
        
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException
    {
        if((limit > 0 && count+1 >= limit) || !iterator.hasNext()) return SKIP_BODY;
        
        item = iterator.next();
        
        index++;
        count++;
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, item, getScopeInt());
        }
        
        if(status != null)
        {
            pageContext.setAttribute(status, getLoopStatus());
        }
        
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException
    {
        if(iterator.hasNext() && iterator instanceof AutoCloseable)
        {
            try
            {
                ((AutoCloseable)iterator).close();
            }
            catch(Exception e) { e.printStackTrace(); }
        }
        
        iterator = null;
        item = null;
        variable = null;
        scope = "page";
        status = null;
        limit = 0;
        skip = 0;
        
        return EVAL_PAGE;
    }
}
