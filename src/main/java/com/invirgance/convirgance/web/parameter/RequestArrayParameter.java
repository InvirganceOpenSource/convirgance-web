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

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.web.http.HttpRequest;
import java.util.Arrays;

/**
 * Allows returning the parameters values of a {@link HttpRequest} as an array.
 * 
 * <pre>
 * Use this parameter type when you need to:
 * - Access multiple values for the same parameter name
 * - Handle repeated query parameters
 * - Process multi-select form fields
 * </pre>
 * 
 * @author jbanes
 */
public class RequestArrayParameter implements Parameter
{
    private String name;
    private String requestName;
    
    /**
     * Gets the name of this parameter.
     * Returns the internal name if set, otherwise falls back to the request parameter name.
     * 
     * @return The parameter name
     */
    @Override
    public String getName()
    {
        if(name != null) return name;
        
        return requestName;
    }

    /**
     * Sets the name of the parameter to retrieve.
     * 
     * @param name Parameter name.
     */
    public void setName(String name)
    {
        this.name = name;
        
        // Fallback to name if the user doesn't set the UrlParameterName
        if(this.requestName == null) this.requestName = name;
    }
    
    /**
     * Returns the parameter name.
     * 
     * @return The name.
     */
    public String getUrlParameterName()
    {
        return requestName;
    }

    /**
     * Sets the URL parameter name to retrieve.
     * 
     * @param urlParamName The name.
     */
    public void setUrlParameterName(String urlParamName)
    {
        this.requestName = urlParamName;
    }

    /**
     * Gets the parameter's values as a {@link JSONArray}.
     * 
     * @param request The request to get the values from.
     * @return An array containing the values.
     */
    @Override
    public Object getValue(HttpRequest request)
    {
        String[] values = request.getParameterValues(requestName);
        JSONArray<String> array = new JSONArray<>();
        
        if(values == null) return array;
        
        array.addAll(Arrays.asList(values));
        
        return array;
    }
    
}
