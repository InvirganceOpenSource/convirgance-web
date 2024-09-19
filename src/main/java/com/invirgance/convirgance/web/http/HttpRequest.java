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
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author jbanes
 */
public class HttpRequest
{
    private Object request;

    public HttpRequest(Object request)
    {
        this.request = request;
    }
    
    private Object execRequestMethod(String methodName, Object... parameters)
    {
        Class clazz = request.getClass();
        Class[] types = new Class[parameters.length];
        
        for(int i=0; i<parameters.length; i++) types[i] = parameters[i].getClass();
        
        try
        {
            return (String)clazz.getMethod(methodName, types).invoke(request, parameters);
        }
        catch(Exception e) { throw new ConvirganceException(e); }
    }
    
    public String getContextPath()
    {
        return (String)execRequestMethod("getContextPath");
    }
    
    // TODO: getCookies()
    
    public long getDateHeader(String name)
    {
        return (long)execRequestMethod("getDateHeader", name);
    }
    
    public String getHeader(String name)
    {
        return (String)execRequestMethod("getHeader", name);
    }
    
    public Iterable<String> getHeaders(String name)
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getHeaders", name);
        
        return Collections.list(enumeration);
    }
    
    public Iterable<String> getHeaderNames(String name)
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getHeaderNames", name);
        
        return Collections.list(enumeration);
    }
    
    public int getIntHeader(String name)
    {
        return Integer.parseInt(getHeader(name));
    }
    
    public long getLongHeader(String name)
    {
        return Long.parseLong(getHeader(name));
    }
    
    public String getMethod()
    {
        return (String)execRequestMethod("getMethod");
    }
    
    public String getPathInfo()
    {
        return (String)execRequestMethod("getPathInfo");
    }
    
    public String getPathTranslated()
    {
        return (String)execRequestMethod("getPathTranslated");
    }
    
    public String getQueryString()
    {
        return (String)execRequestMethod("getQueryString");
    }
    
    // TODO: getRemoteUser
    // TODO: isUserInRole
    // TODO: getUserPrincipal
    // TODO: getRequestedSessionId
    
    public String getRequestURI()
    {
        return (String)execRequestMethod("getRequestURI");
    }
    
    public String getRequestURL()
    {
        return (String)execRequestMethod("getRequestURL");
    }
    
    public String getServletPath()
    {
        return (String)execRequestMethod("getServletPath");
    }
    
    // TODO: getSession(boolean create)
    // TODO: getSession()
    // TODO: isRequestedSessionIdValid()
    // TODO: isRequestedSessionIdFromCookie()
    // TODO: isRequestedSessionIdFromURL()
    
    // TODO: authenticate()
    // TODO: login()
    // TODO: logout()
    
    // TODO: getParts()
    // TODO: getPart(String name)
    
    // TODO: getAttribute(String name)
    // TODO: getAttributeNames()
    
    public String getCharacterEncoding()
    {
        return (String)execRequestMethod("getCharacterEncoding");
    }
    
    // TODO: setCharacterEncoding(String encoding)
    
// TODO: Extend to long? There is a getContentLengthLong() in servlet 3.1
//    public int getContentLength() 
//    {
//        return (int)execRequestMethod("getContentLength");
//    }
  
    public String getContentType()
    {
        return (String)execRequestMethod("getContentType");
    }
    
    public InputStream getInputStream()
    {
        return (InputStream)execRequestMethod("getInputStream");
    }
    
    public String getParameter(String name)
    {
        return (String)execRequestMethod("getParameter", name);
    }
    
    public Iterable<String> getParameterNames()
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getParameterNames");
        
        return Collections.list(enumeration);
    }
    
    public String[] getParameterValues(String name)
    {
        return (String[])execRequestMethod("getParameterValues", name);
    }
    
    public Map<String,String[]> getParameterMap()
    {
        return (Map<String,String[]>)execRequestMethod("getParameterMap");
    }
    
    public String getProtocol()
    {
        return (String)execRequestMethod("getProtocol");
    }
    
    public String getScheme()
    {
        return (String)execRequestMethod("getScheme");
    }
    
    public String getServerName()
    {
        return (String)execRequestMethod("getServerName");
    }
    
    public int getServerPort()
    {
        return (int)execRequestMethod("getServerPort");
    }
    
    // TODO: getReader()
    
    public String getRemoteAddress()
    {
        return (String)execRequestMethod("getRemoteAddr");
    }
    
    public String getRemoteHost()
    {
        return (String)execRequestMethod("getRemoteHost");
    }
    
    public int getRemotePort()
    {
        return (int)execRequestMethod("getRemotePort");
    }
    
    // TODO: setAttribute(name, value)
    // TODO: removeAttribute(name)

    public Locale getLocale()
    {
        return (Locale)execRequestMethod("getLocale");
    }
    
    public Iterable<Locale> getLocales()
    {
        Enumeration<Locale> enumeration = (Enumeration<Locale>)execRequestMethod("getLocales");
        
        return Collections.list(enumeration);
    }
    
    // TODO: isSecure() ? Shouldn't all communication be secure by default?
    // TODO: getRequestDispatcher(path)
    
    
    
    public String getLocalAddress()
    {
        return (String)execRequestMethod("getLocalAddr");
    }
    
    public String getLocalName()
    {
        return (String)execRequestMethod("getLocalName");
    }
    
    public int getLocalPort()
    {
        return (int)execRequestMethod("getLocalPort");
    }
    
    //TODO: getServletContext()
}
