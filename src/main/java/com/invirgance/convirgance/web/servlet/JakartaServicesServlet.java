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

import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.web.service.Service;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author jbanes
 */
public class JakartaServicesServlet extends HttpServlet
{
    private boolean allowGet = true;
    private boolean allowPost = true;
    private boolean allowPut = false;
    private boolean allowDelete = false;
    
    public Service load(HttpServletRequest request)
    {
        String path = request.getServletPath();
        Resource resource;
        
        if(path == null) path = "/";
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        // Transform URI path to file path
        path = request.getServletContext().getRealPath(path);
        resource = new FileSystemResource(path);

        return new GenericXmlApplicationContext(resource).getBean(Service.class);
    }
    
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Service service = load(request);
        
        service.execute(new HttpRequest(request), new HttpResponse(response));
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(!allowGet) throw new ConvirganceException("GET requests are not allowed");
        
        try
        {
            handleRequest(request, response);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            
            throw new ServletException(t);
        }
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
