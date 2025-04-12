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

/**
 * Parameter implementation that extracts values from HTTP request parameters.
 * Provides access to URL query parameters or form field values from 
 * HTTP requests. It supports default values when parameters are not present
 * and allows mapping between internal parameter names and request parameter names.
 * 
 * <pre>
 * This is the most commonly used Parameter implementation, used to:
 * - Extract query string values from GET requests
 * - Access form field values from POST requests
 * - Provide default values for optional parameters
 * </pre>
 * 
 * @author jbanes
 */
public class RequestParameter implements Parameter
{
    private String name;
    private String requestName;
    private String defaultValue;
    
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
     * @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
        
        // Fallback to name if the user doesn't set the UrlParameterName
        if(this.requestName == null) this.requestName = name;
    }
    
    /**
     * Returns the parameter names.
     * 
     * @return The name.
     */
    public String getUrlParameterName()
    {
        return requestName;
    }

    /**
     * Sets the parameter name.
     * 
     * @param urlParamName The name.
     */
    public void setUrlParameterName(String urlParamName)
    {
        this.requestName = urlParamName;
    }

    /**
     * Returns the fallback value if the parameter has none.
     * 
     * @return The default.
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Sets the fallback value when the parameter is null.
     * 
     * @param defaultValue The fallback value.
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the value of a {@link HttpRequest} parameter.
     * 
     * @param request The request.
     * @return The value of the parameter or default.
     */
    @Override
    public Object getValue(HttpRequest request)
    {
        String value = request.getParameter(requestName);
        
        if(value == null) return defaultValue;
        
        return value;
    }
    
}
