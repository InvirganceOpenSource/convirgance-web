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
import jakarta.servlet.jsp.tagext.TagSupport;

/**
 * A custom JSP tag that sets a variable in a specified scope.
 * This tag provides a convenient way to set variables in different scopes
 * (page, request, session, or application) from within a JSP page. It can be used
 * to store any object value under a specified variable name.
 * 
 * @author jbanes
 */
public class SetTag extends TagSupport
{
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
    
    /**
     * Gets the variable name.
     * 
     * @return The name.
     */
    public String getVar()
    {
        return variable;
    }

    /**
     * Sets the variable name to store this with.
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
     * Gets the value to be assigned to the variable.
     * 
     * @return The value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Sets the value to be stored in the variable.
     * 
     * @param value The value to store
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
    
    /**
     * Sets the specified variable to the provided value in the specified scope.
     * 
     * @return SKIP_BODY since this tag doesn't process its body
     * @throws JspException If the variable cannot be set
     */    
    @Override
    public int doStartTag() throws JspException
    {
        pageContext.setAttribute(variable, value, getScopeInt());
        
        return SKIP_BODY;
    }
    
}
