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
import com.invirgance.convirgance.transform.filter.Filter;

/**
 * Filter implementation that evaluates conditions against request parameters.
 * While standard filters test against record data, BindingFilter redirects
 * the test to evaluate against HTTP request parameters stored in thread-local
 * storage. This enables conditional filtering based on request values rather
 * than record content.
 * 
 * <pre>
 * This filter is useful for:
 * - Conditionally processing records based on request parameters
 * - Implementing optional filtering in query interfaces
 * - Creating dynamic filtering behavior controlled by request parameters
 * </pre>
 * 
 * @author jbanes
 */
public class BindingFilter implements Filter
{
    private Filter filter;
    
    public BindingFilter()
    {
    }

    /**
     * Creates a binding filter with the provided filter.
     * 
     * @param filter The filter.
     */
    public BindingFilter(Filter filter)
    {
        this.filter = filter;
    }

    /**
     * Returns the current filter.
     * 
     * @return The filter.
     */
    public Filter getFilter()
    {
        return filter;
    }

    /**
     * Updates or sets the filter.
     * 
     * @param filter The filter.
     */
    public void setFilter(Filter filter)
    {
        this.filter = filter;
    }
    
    @Override
    public boolean test(JSONObject record)
    {
        return filter.test(BindingParameter.bindings.get());
    }
    
}
