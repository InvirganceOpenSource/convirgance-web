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
package com.invirgance.convirgance.web.parameter;

import com.invirgance.convirgance.web.http.HttpRequest;

/**
 * Retrieve the value of an HTTP Header. The returned key defaults to the same
 * name as the header if <code>headerName</code> is not set, but can be overridden 
 * by setting the <code>name</code> parameter.
 * 
 * @author jbanes
 */
public class HeaderParameter implements Parameter
{
    private String name;
    private String headerName;
    private String defaultValue;

    /**
     * Name of the parameter. Defaults to <code>headerName</code> if not set.
     * 
     * @return name of the parameter
     */
    @Override
    public String getName()
    {
        if(name != null) return name;
        
        return headerName;
    }

    /**
     * Sets the name of the parameter. Also sets <code>headerName</code> if
     * not set.
     * 
     * @param name name of the parameter
     */
    public void setName(String name)
    {
        this.name = name;
        
        // Fallback to name if the user doesn't set the header name
        if(this.headerName == null) this.headerName = name;
    }

    /**
     * The header to retrieve from the HTTP request
     * 
     * @return name of the header
     */
    public String getHeaderName()
    {
        return headerName;
    }

    /**
     * Set the header to retrieve from the HTTP request
     * 
     * @param headerName name of the header
     */
    public void setHeaderName(String headerName)
    {
        this.headerName = headerName;
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
        String value = request.getHeader(this.headerName);
        
        if(value == null) return defaultValue;
        
        return value;
    }
    
}
