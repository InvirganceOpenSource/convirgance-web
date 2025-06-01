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
package com.invirgance.convirgance.web.filter;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.ValueGenerator;
import com.invirgance.convirgance.transform.filter.Filter;
import com.invirgance.convirgance.web.service.ServiceState;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Can be set as the value whenever parameter values are needed instead of 
 * record values. Commonly used with {@link Filter}s that support {@link ValueGenerator}. 
 * 
 * @author jbanes
 */
@Wiring
public class ParameterValueGenerator<T> implements ValueGenerator<T>
{
    private String name;

    public ParameterValueGenerator()
    {
    }

    public ParameterValueGenerator(String name) 
    {
        this.name = name;
    }

    /**
     * Get the name of the parameter to return
     * 
     * @return the name of the parameter
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the parameter to return
     * 
     * @param name the name of the parameter
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the parameter value for the configured name
     * 
     * @param record not used
     * @return the parameter value
     */
    @Override
    public T generate(JSONObject record)
    {
        return (T)((JSONObject)ServiceState.get("parameters")).get(this.name);
    }
}
