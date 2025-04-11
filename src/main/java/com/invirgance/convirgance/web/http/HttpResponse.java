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
package com.invirgance.convirgance.web.http;

import com.invirgance.convirgance.ConvirganceException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;

/**
 * Wrapper for HTTP servlet response objects.
 * 
 * <pre>
 * Provides access to common HTTP request elements:
 * - Request parameters and headers
 * - Request path information
 * - Client and server information
 * - Request body content
 * </pre>
 * 
 * @author jbanes
 */
public class HttpResponse
{
    private Object response;

    /**
     * Creates a new HttpResponse based on the object.
     * 
     * @param response The response.
     */
    public HttpResponse(Object response)
    {
        this.response = response;
    }
    
    private Object execResponseMethod(String methodName, Object... parameters)
    {
        Class clazz = response.getClass();
        Class[] types = new Class[parameters.length];
        
        for(int i=0; i<parameters.length; i++) types[i] = parameters[i].getClass();
        
        try
        {
            return clazz.getMethod(methodName, types).invoke(response, parameters);
        }
        catch(Exception e) { throw new ConvirganceException(e); }
    }
    
    // TODO: addCookie
    
    /**
     * Checks if the header with the provided name is set.
     * 
     * @param name The header name.
     * @return True if the object contains name.
     */
    public boolean containsHeader(String name)
    {
        return (boolean)execResponseMethod("containsHeader", name);
    }
    
    // TODO: encodeURL
    // TODO: encodeRedirectURL
    
    /**
     * Sends an the error code with the provided message.
     * 
     * @param code The error code.
     * @param message The message.
     */
    public void sendError(int code, String message)
    {
        execResponseMethod("sendError", code, message);
    }
    
    /**
     * Sends a redirect response with the provided location.
     * 
     * @param location A URL to redirect to.
     */
    public void sendRedirect(String location)
    {
        execResponseMethod("sendRedirect", location);
    }
    
    /**
     * Add a date header using the provided name, assigning time to its value.
     * 
     * @param name The header name.
     * @param time Time as milliseconds from epoch.
     */
    public void addDateHeader(String name, long time)
    {
        execResponseMethod("addDateHeader", name, time);
    }
    
    /**
     * Sets/overwrites the date header with the provided name.
     * Time should be in milliseconds since epoch.
     * 
     * @param name Header name.
     * @param time Milliseconds.
     */
    public void setDateHeader(String name, long time)
    {
        execResponseMethod("setDateHeader", name, time);
    }
    
    /**
     * Adds a header with the given name and value.
     * 
     * @param name Header name.
     * @param value The value for the header.
     */
    public void addHeader(String name, String value)
    {
        execResponseMethod("addHeader", name, value);
    }
    
    /**
     * Sets/overwrites the header with the given name.
     * 
     * @param name The name.
     * @param value The value.
     */
    public void setHeader(String name, String value)
    {
        execResponseMethod("setHeader", name, value);
    }
    
    /**
     * Adds a header with an integer value.
     * 
     * @param name The name.
     * @param value The int value.
     */
    public void addIntHeader(String name, int value)
    {
        execResponseMethod("addIntHeader", name, value);
    }
    
    /**
     * Sets/overwrites the integer headers value.
     * 
     * @param name The name of the header.
     * @param value The new value.
     */
    public void setIntHeader(String name, int value)
    {
        execResponseMethod("setIntHeader", name, value);
    }
    
    /**
     * Adds a header with the provided name, assigning the long value.
     * The format of the value may change depending on the systems locale.
     * 
     * @param name The header name.
     * @param value A long value.
     */
    public void addLongHeader(String name, long value)
    {
        addHeader(name, Long.toString(value));
    }
    
    /**
     * Sets/overwrites the header with the given name.
     * The format of the value may change depending on the systems locale.
     * 
     * @param name The header name.
     * @param value The new value to assign.
     */
    public void setLongHeader(String name, long value)
    {
        setHeader(name, Long.toString(value));
    }
    
    /**
     * Gets the status code.
     * 
     * @return The response status code.
     */
    public int getStatus()
    {
        return (int)execResponseMethod("getStatus");
    }
    
    /**
     * Sets the status code of the response.
     * 
     * @param code The status code.
     */
    public void setStatus(int code)
    {
        execResponseMethod("setStatus", code);
    }
    
    /**
     * Gets the header with the provided name.
     * 
     * @param name The header name.
     * @return The value of the header.
     */
    public String getHeader(String name)
    {
        return (String)execResponseMethod("getHeader", name);
    }
    
    /**
     * An iterable of the values for the header with the given name.
     * 
     * @param name The header name.
     * @return An iterable containing the values
     */
    public Iterable<String> getHeaders(String name)
    {
        return (Collection<String>)execResponseMethod("getHeaders", name);
    }
    
    /**
     * Returns the header names.
     * 
     * @return The header names of the response.
     */
    public Iterable<String> getHeaderNames()
    {
        return (Collection<String>)execResponseMethod("getHeaderNames");
    }
    
    /**
     * Updates the content type value.
     * 
     * @param name The value for content-type.
     */
    public void setContentType(String name)
    {
        execResponseMethod("setContentType", name);
    }
    
    /**
     * Gets a {@link OutputStream} to the responses body.
     * 
     * @return An output stream to the body.
     */
    public OutputStream getOutputStream()
    {
        return new CloseInterceptOutputStream((OutputStream)execResponseMethod("getOutputStream"));
    }
    
    /**
     * Gets the responses value of locale.
     * 
     * @return The locale
     */
    public Locale getLocale()
    {
        return (Locale)execResponseMethod("getLocale");
    }
    
    /**
     * Sets the responses locale.
     * 
     * @param locale The language locale.
     */
    public void setLocale(Locale locale)
    {
        execResponseMethod("setLocale");
    }
}
