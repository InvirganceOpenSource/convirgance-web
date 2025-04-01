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
 *
 * @author jbanes
 */
public class ValueTag extends TagSupport implements ValueTypeTag
{
    private Object value;
    private Object defaultValue;
    
    private Tag parent;

    public Object getValue()
    {   
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public Object getDefault()
    {
        return defaultValue;
    }

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
        if(parent instanceof ValueTypeTag)
        {
            ((ValueTypeTag)parent).setValue(value != null ? value : defaultValue);
        }
        
        this.value = null;
        this.defaultValue = null;
        
        return EVAL_PAGE;
    }
    
    @Override
    public void release()
    {
        this.value = null;
        this.defaultValue = null;
        
        super.release();
    }
}
