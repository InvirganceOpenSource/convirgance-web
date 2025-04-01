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

import com.invirgance.convirgance.json.JSONParser;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyContent;
import jakarta.servlet.jsp.tagext.BodyTag;
import jakarta.servlet.jsp.tagext.Tag;
import java.io.IOException;

/**
 *
 * @author jbanes
 */
public class JSONTag implements BodyTag
{
    private BodyContent content;
    private PageContext context;
    private Tag parent;
    
    private String variable;
    private String scope = "page";
    
    private Object value;
    
    
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
    
    @Override
    public void setPageContext(PageContext context)
    {
        this.context = context;
    }

    @Override
    public void setParent(Tag tag)
    {
        this.parent = tag;
    }

    @Override
    public Tag getParent()
    {
        return this.parent;
    }

    @Override
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException
    {
        if(parent instanceof ValueTag)
        {
            ((ValueTag)parent).setValue(value);
        }
        
        this.value = null;
        
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        this.content = null;
        this.context = null;
        this.value = null;
    }

    @Override
    public void setBodyContent(BodyContent content)
    {
        this.content = content;
    }

    @Override
    public void doInitBody() throws JspException
    {
    }

    @Override
    public int doAfterBody() throws JspException
    {
        try
        {
            this.value = new JSONParser(content.getReader()).parse();

            if(this.variable != null) 
            {
                context.setAttribute(this.variable, this.value, getScopeInt());
            }
        }
        catch(IOException e) { throw new JspException(e); }

        return EVAL_BODY_INCLUDE;
    }
    
}
