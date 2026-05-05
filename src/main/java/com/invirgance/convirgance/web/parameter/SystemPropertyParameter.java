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
package com.invirgance.convirgance.web.parameter;

import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Retrieve the value of a System property. The value of <code>name</code> is
 * used as the <code>property</code> if <code>name</code> is not set, but can be overridden 
 * by setting the <code>name</code> parameter.
 * 
 * @author jbanes
 */
@Wiring
public class SystemPropertyParameter implements Parameter
{
    private String property;
    private String name;
    private String defaultValue;

    /**
     * Key to retrieve from system properties
     * 
     * @return the system property key
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * Sets the key to retrieve from system properties
     * 
     * @param property the system property key
     */
    public void setProperty(String property)
    {
        if(this.name == null) this.name = property;
        
        this.property = property;
    }
    
    /**
     * Name of the parameter. Defaults to <code>property</code> if not set.
     * 
     * @return name of the parameter
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the parameter. Also sets <code>property</code> if
     * not set.
     * 
     * @param name name of the parameter
     */
    public void setName(String name)
    {
        if(property == null) property = name;
        
        this.name = name;
    }

    /**
     * The value to return if the header is not found
     * 
     * @return default value to return
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Set the value to return if the header is not found
     * 
     * @param defaultValue default value to return
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object getValue(HttpRequest request)
    {
        String value = System.getProperty(this.property);
        
        if(value == null) return defaultValue;
        
        return value;
    }
    
}
