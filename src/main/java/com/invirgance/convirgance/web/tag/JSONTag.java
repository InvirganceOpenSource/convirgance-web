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
 * A custom JSP tag that parses JSON content and makes it available as a 
 * variable. This tag processes its body content as JSON text, parses it into a
 * Java object structure using {@link JSONParser}, and makes the resulting 
 * object available as a page variable or passes it to a parent tag that 
 * implements {@link ValueTypeTag}.
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
    private String parse;
    
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
     * Gets the variable name where the parsed JSON will be stored.
     * 
     * @return The variable.
     */
    public String getVar()
    {
        return variable;
    }

    /**
     * Sets the variable name where the parsed JSON will be stored.
     * 
     * @param variable The variable name
     */
    public void setVar(String variable)
    {
        this.variable = variable;
    }

    /**
     * Gets the JSON string to be parsed instead of using the body of the tag.
     * 
     * @return the json string
     */
    public String getParse()
    {
        return parse;
    }

    /**
     * Sets a JSON string to be parsed instead of using the contents of the tag
     * body. This is useful when a data record contains a JSON string which needs
     * to be parsed as the body only allows static JSON.
     * 
     * @param parse the json string to parse
     */
    public void setParse(String parse)
    {
        this.parse = parse;
    }

    /**
     * Gets the scope where the variable will be stored.
     * 
     * @return The scope name.
     */
    public String getScope()
    {
        return scope;
    }

    /**
     * Sets the scope where the variable will be stored.
     * Valid options are: page, request, session, application.
     * 
     * @param scope The scope name.
     */
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
        ValueTypeTag parent = findParent();
        
        if(parse != null) 
        {
            try
            {
                this.value = new JSONParser(parse).parse();

                if(this.variable != null) 
                {
                    context.setAttribute(this.variable, this.value, getScopeInt());
                }

                if(parent != null)
                {
                    parent.setValue(this.value);
                }
            }
            catch(IOException e) { throw new JspException(e); }
            
            return SKIP_BODY;
        }
        
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException
    {
        if(parent instanceof ValueTypeTag)
        {
            ((ValueTypeTag)parent).setValue(value);
        }
        
        this.value = null;
        this.parse = null;
        
        return EVAL_PAGE;
    }

    @Override
    public void release()
    {
        this.content = null;
        this.context = null;
        this.value = null;
        this.parse = null;
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
        ValueTypeTag parent = findParent();
        
        try
        {
            this.value = new JSONParser(content.getReader()).parse();

            if(this.variable != null) 
            {
                context.setAttribute(this.variable, this.value, getScopeInt());
            }
            
            if(parent != null)
            {
                parent.setValue(this.value);
            }
        }
        catch(IOException e) { throw new JspException(e); }

        return EVAL_BODY_INCLUDE;
    }
    
}
