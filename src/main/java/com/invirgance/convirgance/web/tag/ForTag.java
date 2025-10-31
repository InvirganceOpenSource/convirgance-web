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

/**
 *
 * @author jbanes
 */
public class ForTag extends BodyTagSupport
{
    private String variable;
    private String scope = "page";
    private int begin;
    private int end;
    private int step = 1; // Defaults to 1
    
    private int index;
    
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

    public int getBegin()
    {
        return begin;
    }

    public void setBegin(int begin)
    {
        this.begin = begin;
    }

    public int getEnd()
    {
        return end;
    }

    public void setEnd(int end)
    {   
        this.end = end;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int step) throws JspException
    {
        if(end < 1) throw new JspException("Invalid step: " + step + ". Step must be a positive number greater than 1.");
        
        this.step = step;
    }
    
    @Override
    public int doStartTag() throws JspException
    {
        index = begin;
        
        if(end < begin) throw new JspException("Invalid loop: " + begin + " - " + end + ". End must be larger than start.");
        if(index >= end) return SKIP_BODY;
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, index, getScopeInt());
        }
        
        return EVAL_BODY_INCLUDE;
    }
    
    @Override
    public int doAfterBody() throws JspException
    {
        if(index >= end) return SKIP_BODY;
        
        
        index += step;
        
        if(variable != null)
        {
            pageContext.setAttribute(variable, index, getScopeInt());
        }
        
        return EVAL_BODY_AGAIN;
    }

    @Override
    public int doEndTag() throws JspException
    {
        variable = null;
        scope = "page";
        begin = 0;
        end = 0;
        step = 1;
        index = 0;
        
        return EVAL_PAGE;
    }
}
