/*
 * The MIT License
 *
 * Copyright 2026 jbanes.
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
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 *
 * @author jbanes
 */
public class IncludeTag extends BodyTagSupport
{
    private String page;
    private boolean flush;
    private JSONObject args;

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    public boolean isFlush()
    {
        return flush;
    }

    public void setFlush(boolean flush)
    {
        this.flush = flush;
    }
    
    public Object getArgument(String arg)
    {
        return args.get(arg);
    }
    
    public void setArgument(String arg, Object value) throws JspException
    {
        if(args.containsKey(arg)) throw new JspException(arg + " is already set!");
        
        args.put(arg, value);
    }
    
    public boolean hasArgument(String arg)
    {
        return args.containsKey(arg);
    }

    @Override
    public int doStartTag() throws JspException
    {
        args = new JSONObject();
        
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException
    {
        return SKIP_BODY;
    }
    
    @Override
    public int doEndTag() throws JspException
    {
        ServletRequest request = this.pageContext.getRequest();
        Object value = request.getAttribute("args");
        
        try
        {
            request.setAttribute("args", this.args);
            this.pageContext.include(page, flush);
            request.setAttribute("args", value);
        }
        catch(ServletException | IOException e) 
        {
            throw new JspException(e);
        }
        
        this.page = null;
        this.flush = false;
        this.args = null;
        
        return EVAL_PAGE;
    }
    
}
