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
 *
 * @author jbanes
 */
public class HttpResponse
{
    private Object response;

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
    
    public boolean containsHeader(String name)
    {
        return (boolean)execResponseMethod("containsHeader", name);
    }
    
    // TODO: encodeURL
    // TODO: encodeRedirectURL
    
    public void sendError(int code, String message)
    {
        execResponseMethod("sendError", code, message);
    }
    
    public void sendRedirect(String location)
    {
        execResponseMethod("sendRedirect", location);
    }
    
    public void addDateHeader(String name, long time)
    {
        execResponseMethod("addDateHeader", name, time);
    }
    
    public void setDateHeader(String name, long time)
    {
        execResponseMethod("setDateHeader", name, time);
    }
    
    public void addHeader(String name, String value)
    {
        execResponseMethod("addHeader", name, value);
    }
    
    public void setHeader(String name, String value)
    {
        execResponseMethod("setHeader", name, value);
    }
    
    public void addIntHeader(String name, int value)
    {
        execResponseMethod("addIntHeader", name, value);
    }
    
    public void setIntHeader(String name, int value)
    {
        execResponseMethod("setIntHeader", name, value);
    }
    
    public void addLongHeader(String name, long value)
    {
        addHeader(name, Long.toString(value));
    }
    
    public void setLongHeader(String name, long value)
    {
        setHeader(name, Long.toString(value));
    }
    
    public int getStatus()
    {
        return (int)execResponseMethod("getStatus");
    }
    
    public void setStatus(int code)
    {
        execResponseMethod("setStatus", code);
    }
    
    public String getHeader(String name)
    {
        return (String)execResponseMethod("getHeader", name);
    }
    
    public Iterable<String> getHeaders(String name)
    {
        return (Collection<String>)execResponseMethod("getHeaders", name);
    }
    
    public Iterable<String> getHeaderNames()
    {
        return (Collection<String>)execResponseMethod("getHeaderNames");
    }
    
    public void setContentType(String name)
    {
        execResponseMethod("setContentType", name);
    }
    
    public OutputStream getOutputStream()
    {
        return new CloseInterceptOutputStream((OutputStream)execResponseMethod("getOutputStream"));
    }
    
    public Locale getLocale()
    {
        return (Locale)execResponseMethod("getLocale");
    }
    
    public void setLocale(Locale locale)
    {
        execResponseMethod("setLocale");
    }
}
