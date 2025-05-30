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
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

/**
 * A custom JSP tag that defines a key-value pair for JSON objects.
 * This tag is designed to be used within parent tags that implement the 
 * {@link KeyValueTypeTag} interface, such as {@link ObjectTag}
 * or {@link ServiceTag}. It assigns a named value to a key in the parent's
 * data structure.
 * 
 * @author jbanes
 */
public class KeyTag extends TagSupport implements ValueTypeTag
{
    private String name;
    private Object value;
    private Object defaultValue;
    
    private Tag parent;
    
    private KeyValueTypeTag findParent()
    {
        Tag parent = getParent();
        
        while(parent != null)
        {
            if(parent instanceof KeyValueTypeTag) return (KeyValueTypeTag)parent;
            
            parent = parent.getParent();
        }
        
        return null;
    }

    /**
     * Gets the name of the key in the key-value pair.
     * 
     * @return The name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the key in the key-value pair.
     * 
     * @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets the value associated with this key.
     * 
     * @return The value for this key
     */
    @Override
    public Object getValue()
    {   
        return value;
    }
    
    /**
     * Sets the value associated with this key.
     * 
     * @param value The value for this key
     */
    @Override
    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * Gets the default value for this key.
     * 
     * @return The default.
     */
    public Object getDefault()
    {
        return defaultValue;
    }

    /**
     * Sets the default value if none is provided later on.
     * 
     * @param defaultValue The default.
     */
    public void setDefault(Object defaultValue)
    {
        this.defaultValue = defaultValue;
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
        return EVAL_BODY_INCLUDE;
    }
    
    @Override
    public int doEndTag() throws JspException
    {
        KeyValueTypeTag parent = findParent();
        
        if(parent != null)
        {
            parent.set(name, value != null ? value : defaultValue);
        }
        
        this.name = null;
        this.value = null;
        this.defaultValue = null;
        
        return EVAL_PAGE;
    }
    
    @Override
    public void release()
    {
        this.name = null;
        this.value = null;
        this.defaultValue = null;
        
        super.release();
    }
}
