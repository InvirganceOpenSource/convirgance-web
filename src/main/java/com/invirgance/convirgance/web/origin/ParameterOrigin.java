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
package com.invirgance.convirgance.web.origin;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.source.ByteArraySource;
import com.invirgance.convirgance.source.Source;
import com.invirgance.convirgance.web.http.HttpRequest;
import java.io.IOException;

/**
 * Origin implementation that extracts data from a specific HTTP request 
 * parameter.
 * 
 * <pre>
 * Use this origin when you need to:
 * - Process data submitted in a specific request parameter
 * - Handle form field submissions containing structured data
 * - Extract and process URL-encoded parameter values
 * </pre>
 * 
 * @author jbanes
 */
public class ParameterOrigin implements Origin
{
    private String name;

    /**
     * The name of the parameter.
     * 
     * @return The current name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the parameter to retrieve.
     * 
     * @param name The parameter names
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets the value of the parameter assigned with {@link #setName}.
     * 
     * @param request The {@link HttpRequest}. 
     * @param parameters _unused.
     * @return The ByteArraySource for the parameters value.
     */
    @Override
    public Source getOrigin(HttpRequest request, JSONObject parameters)
    {
        try
        {
            return new ByteArraySource(request.getParameter(name).getBytes("UTF-8"));
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }
}
