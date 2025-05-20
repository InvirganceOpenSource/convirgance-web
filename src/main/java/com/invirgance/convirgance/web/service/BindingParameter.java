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
package com.invirgance.convirgance.web.service;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.filter.CoerciveComparator;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Provides access to request parameters within the data processing pipeline.
 * BindingParameter acts as a reference to a specific request parameter stored
 * in thread-local storage.
 * 
 * <pre>
 * Common uses include:
 * - Accessing request values in filter conditions
 * - Referencing request parameters in transformers
 * - Using request parameters as values in expressions
 * </pre>
 * 
 * @author jbanes
 */
@Wiring
public class BindingParameter implements Comparable
{
    static final CoerciveComparator comparator = new CoerciveComparator();
    static final InheritableThreadLocal<JSONObject> bindings = new InheritableThreadLocal<>();
    
    private String key;

    public BindingParameter()
    {
    }
    
    /**
     * Creates a new BindingParameter with the specified key.
     *
     * @param key The parameter key to retrieve from request parameters
     */
    public BindingParameter(String key)
    {
        this.key = key;
    }
    
    /**
     * Gets the parameter key.
     *
     * @return The parameter key
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * Sets the parameter key to retrieve from request parameters.
     *
     * @param key The parameter key
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    
    /**
     * Retrieves the value of the parameter from thread-local request parameters.
     *
     * @return The parameter value
     */
    public Object getValue()
    {
        return bindings.get().get(key);
    }

    @Override
    public int hashCode()
    {
        return getValue().hashCode();
    }
    
    /**
     * Compares the parameter value with another object for equality.
     *
     * @param obj The object to compare with
     * @return {@code true} if the parameter value equals the object
     */
    @Override
    public boolean equals(Object obj)
    {
        return getValue().equals(obj);
    }

    @Override
    public String toString()
    {
        return getValue().toString();
    }

    /**
     * This enables BindingParameter instances to be used directly in comparison
     * operations with values of different types.
     * 
     * @param object The object to compare with
     * @return A negative integer, zero, or a positive integer if this value 
     * is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(Object object)
    {
        return comparator.compare(this, object);
    }
}
