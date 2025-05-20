/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.web.parameter;

import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Parameter implementation that provides a fixed, predefined value.
 * StaticParameter supplies a constant value that doesn't depend on the HTTP request.
 * This is useful for providing configuration values, default settings, or context
 * information that should be available to services and transformers.
 * 
 * <pre>
 * Use this parameter type when you need to:
 * - Include configuration constants in service processing
 * - Add metadata that isn't part of the request
 * - Provide environment or context information to services
 * </pre>
 * 
 * @author jbanes
 */
@Wiring
public class StaticParameter implements Parameter
{
    private String name;
    private Object value;

    @Override
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Sets the parameter name.
     * 
     * @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the value regardless of the request.
     * 
     * @param request The request.
     * @return The value.
     */
    @Override
    public Object getValue(HttpRequest request)
    {
        return this.value;
    }

    /**
     * Sets the value.
     * 
     * @param value The value.
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}
