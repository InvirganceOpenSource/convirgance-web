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
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Wrapper for HTTP servlet request objects.
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
public class HttpRequest
{
    private Object request;

    /**
     * Creates a HttpRequest using the provided object.
     * 
     * @param request An objects with request options.
     */
    public HttpRequest(Object request)
    {
        this.request = request;
    }
    
    private Object execMethod(Object obj, String methodName, Object... parameters)
    {
        Class clazz = obj.getClass();
        Class[] types = new Class[parameters.length];
        
        for(int i=0; i<parameters.length; i++) types[i] = parameters[i].getClass();
        
        try
        {
            return clazz.getMethod(methodName, types).invoke(obj, parameters);
        }
        catch(Exception e) { throw new ConvirganceException(e); }
    }
    
    private Object execRequestMethod(String methodName, Object... parameters)
    {
        Class clazz = request.getClass();
        Class[] types = new Class[parameters.length];
        
        for(int i=0; i<parameters.length; i++) types[i] = parameters[i].getClass();
        
        try
        {
            return clazz.getMethod(methodName, types).invoke(request, parameters);
        }
        catch(Exception e) { throw new ConvirganceException(e); }
    }
    
    /**
     * Gets the context/request path.
     * 
     * @return The path.
     */
    public String getContextPath()
    {
        return (String)execRequestMethod("getContextPath");
    }
    
    // TODO: getCookies()
    
    /**
     * Gets epoch for the provided date header.
     * 
     * @param name A date header.
     * @return The date.
     */
    public long getDateHeader(String name)
    {
        return (long)execRequestMethod("getDateHeader", name);
    }
    
    /**
     * Returns the value of the provided header.
     * For example "Accept-Encoding" would return the supported types of 
     * compression encoding.
     * 
     * @param name A header name.
     * @return The value.
     */
    public String getHeader(String name)
    {
        return (String)execRequestMethod("getHeader", name);
    }
    
    /**
     * Returns the value of the provided header.
     * For example "Accept-Encoding" would return the supported types of 
     * compression encoding.
     * 
     * @param name A header name.
     * @return The value as an {@link Iterable}.
     */    
    public Iterable<String> getHeaders(String name)
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getHeaders", name);
        
        return Collections.list(enumeration);
    }
    
    /**
     * Returns the request headers with the name.
     * 
     * @param name A header name.
     * @return The value as an {@link Iterable}.
     */      
    public Iterable<String> getHeaderNames(String name)
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getHeaderNames", name);
        
        return Collections.list(enumeration);
    }
    
    /**
     * Returns the request header with the name.
     * 
     * @param name A header name.
     * @return The value parsed as an int.
     */      
    public int getIntHeader(String name)
    {
        return Integer.parseInt(getHeader(name));
    }
    
    /**
     * Returns the request header with the name.
     * 
     * @param name A header name.
     * @return The value parsed as an long.
     */          
    public long getLongHeader(String name)
    {
        return Long.parseLong(getHeader(name));
    }

    /**
     * Returns the request method.
     * 
     * @return The method.
     */          
    public String getMethod()
    {
        return (String)execRequestMethod("getMethod");
    }
    
    /**
     * Returns the path info including extra information after the path.
     * 
     * @return The request path.
     */     
    public String getPathInfo()
    {
        return (String)execRequestMethod("getPathInfo");
    }
    
    /**
     * Returns any extra path information after the servlet name but before 
     * the query string, and translated it into a real path
     * 
     * @return The path.
     */         
    public String getPathTranslated()
    {
        return (String)execRequestMethod("getPathTranslated");
    }
    
    /**
     * Returns query information included after the request path.
     * 
     * @return A string.
     */
    public String getQueryString()
    {
        return (String)execRequestMethod("getQueryString");
    }
    
    // TODO: getRemoteUser
    // TODO: isUserInRole
    // TODO: getUserPrincipal
    // TODO: getRequestedSessionId
    
    /**
     * Returns the full request path, including the protocol but not the 
     * query string.
     * 
     * @return The URI.
     */
    public String getRequestURI()
    {
        return (String)execRequestMethod("getRequestURI");
    }

    /**
     * Returns the request URL.
     * 
     * @return The URL.
     */    
    public String getRequestURL()
    {
        return (String)execRequestMethod("getRequestURL");
    }
    
    /**
     * Returns the servlet path.
     * 
     * @return The path.
     */      
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
    
    /**
     * Returns the character encoding provided in the request.
     * 
     * @return The character encoding.
     */    
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
  
    /**
     * Returns the content-type request header.
     * 
     * @return The content type.
     */
    public String getContentType()
    {
        return (String)execRequestMethod("getContentType");
    }
    
    /**
     * Returns an {@link InputStream} of the requests body.
     * 
     * @return The body.
     */
    public InputStream getInputStream()
    {
        return (InputStream)execRequestMethod("getInputStream");
    }
    
    /**
     * Returns the value of the parameter.
     * 
     * @param name Parameter name.
     * @return The value.
     */
    public String getParameter(String name)
    {
        return (String)execRequestMethod("getParameter", name);
    }
    
    /**
     * Returns the parameters names.
     * 
     * @return An {@link Iterable} of the parameter names. 
     */
    public Iterable<String> getParameterNames()
    {
        Enumeration<String> enumeration = (Enumeration<String>)execRequestMethod("getParameterNames");
        
        return Collections.list(enumeration);
    }
    
    /**
     * Returns the values for the parameter with the provided name.
     * 
     * @param name The parameter name.
     * @return An array containing the values.
     */
    public String[] getParameterValues(String name)
    {
        return (String[])execRequestMethod("getParameterValues", name);
    }
    
    /**
     * Returns a map representing the request information.
     * 
     * @return The request.
     */
    public Map<String,String[]> getParameterMap()
    {
        return (Map<String,String[]>)execRequestMethod("getParameterMap");
    }
    
    /**
     * Returns the protocol used for the request.
     * 
     * @return The protocol.
     */    
    public String getProtocol()
    {
        return (String)execRequestMethod("getProtocol");
    }
    
    /**
     * Returns the scheme used for the request.
     * Example: HTTP, HTTPS
     * 
     * @return The scheme.
     */
    public String getScheme()
    {
        return (String)execRequestMethod("getScheme");
    }
    
    /**
     * Returns the host name of the request.
     * Could also be the "server name" or the IP address.
     * 
     * @return The value.
     */    
    public String getServerName()
    {
        return (String)execRequestMethod("getServerName");
    }
    
    /**
     * Returns the port the request was sent from.
     * 
     * @return The port number.
     */    
    public int getServerPort()
    {
        return (int)execRequestMethod("getServerPort");
    }
    
    // TODO: getReader()
    
    /**
     * Returns the IP address of the client. 
     * If the client is behind a proxy ex Cloudflare, that IP would be 
     * returned instead.
     * 
     * @return The IP address.
     */    
    public String getRemoteAddress()
    {
        return (String)execRequestMethod("getRemoteAddr");
    }
    
    /**
     * Returns the fully qualified host name the request was made from.
     * 
     * @return The FQ name.
     */
    public String getRemoteHost()
    {
        return (String)execRequestMethod("getRemoteHost");
    }
    
    /**
     * Returns the port the clients request was made from.
     * 
     * @return The port.
     */
    public int getRemotePort()
    {
        return (int)execRequestMethod("getRemotePort");
    }
    
    // TODO: setAttribute(name, value)
    // TODO: removeAttribute(name)

    /**
     * Returns the language local of the request.
     * 
     * @return The language.
     */
    public Locale getLocale()
    {
        return (Locale)execRequestMethod("getLocale");
    }
    
    /**
     * Returns the accepted languages of the client.
     * 
     * @return The languages.
     */
    public Iterable<Locale> getLocales()
    {
        Enumeration<Locale> enumeration = (Enumeration<Locale>)execRequestMethod("getLocales");
        
        return Collections.list(enumeration);
    }
    
    // TODO: isSecure() ? Shouldn't all communication be secure by default?
    // TODO: getRequestDispatcher(path)
    
    
    /**
     * Returns the IP the request was received on.
     * 
     * @return The IP.
     */
    public String getLocalAddress()
    {
        return (String)execRequestMethod("getLocalAddr");
    }
    
    /**
     * Returns the host name the request was received on.
     * 
     * @return The host name.
     */
    public String getLocalName()
    {
        return (String)execRequestMethod("getLocalName");
    }
    
    /**
     * Returns the port the request was received on.
     * 
     * @return Port number.
     */
    public int getLocalPort()
    {
        return (int)execRequestMethod("getLocalPort");
    }
    
    //TODO: getServletContext()
    
    
    /**
     * Returns the local path for the specified path
     * 
     * @param path path to 
     * @return 
     */
    public File getFileByPath(String path)
    {
        String context = getContextPath();
        Object servletContext = execRequestMethod("getServletContext");
        String filePath = (String)execMethod(servletContext, "getRealPath", path);
        
        if(filePath == null) return null;
        
        return new File(filePath);
    }
}
