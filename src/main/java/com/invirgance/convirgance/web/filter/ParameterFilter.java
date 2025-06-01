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

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.filter.Filter;
import com.invirgance.convirgance.web.service.ServiceState;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Filter on parameter values. In most contexts, filtering happens on a stream
 * of records. This class interrupts the stream and sends the parameters record
 * to the wrapped filter instead of the record stream.
 * 
 * @author jbanes
 */
@Wiring
public class ParameterFilter implements Filter
{
    private Filter filter;

    public ParameterFilter()
    {
    }

    public ParameterFilter(Filter filter)
    {
        this.filter = filter;
    }

    /**
     * Return the wrapped filter
     * 
     * @return the wrapped filter 
     */
    public Filter getFilter()
    {
        return filter;
    }

    /**
     * Set the filter to wrap and call with the parameters record
     * 
     * @param filter the filter to wrap
     */
    public void setFilter(Filter filter)
    {
        this.filter = filter;
    }

    @Override
    public boolean test(JSONObject record)
    {
        record = (JSONObject)ServiceState.get("parameters");
        
        return filter.test(record);
    }
    
}
