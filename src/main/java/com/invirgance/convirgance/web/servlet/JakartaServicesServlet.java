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
package com.invirgance.convirgance.web.servlet;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.service.Service;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


/**
 * Servlet implementation for Jakarta EE environments that routes HTTP 
 * requests to services. This servlet acts as the entry point for web requests 
 * in Jakarta EE applications HTTP method support can be configured via 
 * initialization parameters.
 * 
 * @author jbanes
 */
public class JakartaServicesServlet extends HttpServlet
{
    private boolean allowGet = true;
    private boolean allowPost = true;
    private boolean allowPut = true;
    private boolean allowDelete = true;
    
    private ServiceLoader loader = ServiceLoader.getInstance();
    
    private void initMethods(String list) throws ServletException
    {
        String[] methods = list.split(",");
        
        if(methods.length < 1) return;
        
        // Reset the allows
        allowGet = false;
        allowPost = false;
        allowPut = false;
        allowDelete = false;
        
        for(String method : methods)
        {
            switch(method.trim().toLowerCase())
            {
                case "get":
                    allowGet = true;
                    break;
                    
                case "post":
                    allowPost = true;
                    break;
                    
                case "put":
                    allowPut = true;
                    break;
                    
                case "delete":
                    allowDelete = true;
                    break;
                    
                default:
                    throw new ServletException("Unsupported HTTP method " + method);
            }
        }
    }
    
    /**
     * Initializes the servlet with the configuration.
     * 
     * @throws ServletException If an error occurs.
     */
    @Override
    public void init() throws ServletException
    {
        ServletConfig config = getServletConfig();
        List<String> names = Collections.list(config.getInitParameterNames());
        
        for(String name : names)
        {
            switch(name.toLowerCase())
            {
                case "methods":
                    initMethods(config.getInitParameter(name));
                    break;
            }
        }
    }
    
    /**
     * Handles/loads the request.
     * 
     * @param servletRequest A {@link HttpServletRequest}
     * @param servletResponse a {@link HttpServletResponse}
     * @throws ServletException If the request cannot be executed correctly.
     * @throws IOException Missing service XML.
     */
    public void handleRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException
    {
        HttpRequest request = new HttpRequest(servletRequest);
        HttpResponse response = new HttpResponse(servletResponse);
        
        ServiceState.set("request", request);
        ServiceState.set("response", response);
            
        Service service = loader.get(servletRequest);
        
        if(service == null)
        {
            System.err.println("Unable to find service XML file at " + servletRequest.getPathInfo() + ".xml");
            servletResponse.sendError(404, "Service not found");
            ServiceState.release();
            return;
        }
        
        try
        {
            service.execute(request, response);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            
            if(!servletResponse.isCommitted()) 
            {
                servletResponse.resetBuffer();
                servletResponse.reset();
            }
            
            throw new ServletException(t);
        }
        finally
        {
            ServiceState.release();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(!allowGet) throw new ConvirganceException("GET requests are not allowed");
        
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(!allowPost) throw new ConvirganceException("POST requests are not allowed");
        
        handleRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(!allowPut) throw new ConvirganceException("PUT requests are not allowed");
        
        handleRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(!allowDelete) throw new ConvirganceException("DELETE requests are not allowed");
        
        handleRequest(request, response);
    }
}
