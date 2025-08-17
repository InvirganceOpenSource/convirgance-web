/*
 * The MIT License
 *
 * Copyright 2024 jbanes.
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
package com.invirgance.convirgance.web.servlet;

import com.invirgance.convirgance.source.FileSource;
import com.invirgance.convirgance.web.service.Service;
import com.invirgance.convirgance.wiring.XMLWiringParser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbanes
 */
public class ServiceLoader
{
    private static ServiceLoader instance;
    
    private List<ServiceDescriptor> cache = new ArrayList<>();
    
    /**
     * Returns the current service loader or new instance.
     * 
     * @return The current or a new instance.
     */
    public static ServiceLoader getInstance()
    {
        if(instance == null) instance = new ServiceLoader();
        
        return instance;
    }
    
    /**
     * Returns a {@link ServiceDescriptor} for the request and path.
     * 
     * @param request The jakarta {@link jakarta.servlet.http.HttpServletRequest}.
     * @param path The path.
     * @return A ServiceDescriptor.
     */
    public ServiceDescriptor load(jakarta.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor descriptor;
        String context = request.getContextPath();
        String filePath;
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) filePath = path + "wiring.xml";
        else if(!path.endsWith(".xml")) filePath = path + ".xml";
        else filePath = path;

        // Transform URI path to file path
        filePath = request.getServletContext().getRealPath(filePath);

        if(filePath == null || !new File(filePath).exists()) return null;
        if(path.endsWith("/wiring.xml")) path = path.substring(0, path.length() - "/wiring.xml".length());
        if(path.endsWith(".xml"))  path = path.substring(0, path.length() - ".xml".length());

        descriptor = new ServiceDescriptor(new File(filePath), path);
        
        this.cache.add(descriptor);
        
        return descriptor;
    }
    
    
    /**
     * Returns a {@link ServiceDescriptor} for the request and path.
     * 
     * @param request The jakarta {@link jakarta.servlet.http.HttpServletRequest}.
     * @return A ServiceDescriptor.
     */
    public ServiceDescriptor load(jakarta.servlet.http.HttpServletRequest request)
    {
        return load(request, request.getRequestURI());
    }

    /**
     * Returns a {@link Service} for the request and path.
     * 
     * @param request The jakarta {@link jakarta.servlet.http.HttpServletRequest}.
     * @param path The path.
     * @return A Service.
     */    
    public Service get(jakarta.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor loaded;
        String context = request.getContextPath();

        if(path.startsWith(context)) path = path.substring(context.length());

        do
        {
            for(ServiceDescriptor descriptor : this.cache)
            {
                if(descriptor.path.equals(path)) return descriptor.getService();
                if(descriptor.path.equals(path + ".xml")) return descriptor.getService();
                if(path.endsWith("/") && descriptor.path.equals(path + "wiring.xml")) return descriptor.getService();
            }

            loaded = load(request, path);

            if(loaded == null && path.contains("/")) 
            {
                if(path.endsWith("/")) path = path.substring(0, path.lastIndexOf('/'));
                else path = path.substring(0, path.lastIndexOf('/')+1);
            }
        }
        while(loaded == null && path.length() > 0 && path.contains("/"));

        if(loaded == null) return null;

        return loaded.getService();
    }
    
    /**
     * Returns a {@link Service} for the request.
     * 
     * @param request The jakarta {@link jakarta.servlet.http.HttpServletRequest}.
     * @return A Service.
     */        
    public Service get(jakarta.servlet.http.HttpServletRequest request)
    {
        return get(request, request.getRequestURI());
    }
    
    /**
     * Returns a {@link ServiceDescriptor} for the {@link javax.servlet.http.HttpServletRequest}
     * 
     * @param request The request.
     * @param path The path.
     * @return The {@link ServiceDescriptor}.
     */
    public ServiceDescriptor load(javax.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor descriptor;
        String context = request.getContextPath();
        String filePath;
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "wiring.xml";
        else if(!path.endsWith(".xml")) path += ".xml";

        // Transform URI path to file path
        filePath = request.getServletContext().getRealPath(path);

        if(filePath == null || !new File(filePath).exists()) return null;
        
        descriptor = new ServiceDescriptor(new File(filePath), path);
        
        this.cache.add(descriptor);
        
        return descriptor;
    }
    
    
    /**
     * Returns a {@link ServiceDescriptor} for the {@link javax.servlet.http.HttpServletRequest}
     * 
     * @param request The request.
     * @return The {@link ServiceDescriptor}.
     */
    public ServiceDescriptor load(javax.servlet.http.HttpServletRequest request)
    {
        return load(request, request.getRequestURI());
    }
    
    /**
     * Returns a {@link Service} for the {@link javax.servlet.http.HttpServletRequest}
     * 
     * @param request The request.
     * @param path The path.
     * @return The {@link Service}.
     */    
    public Service get(javax.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor loaded;
        String context = request.getContextPath();

        if(path.startsWith(context)) path = path.substring(context.length());

        do
        {
            for(ServiceDescriptor descriptor : this.cache)
            {
                if(descriptor.path.equals(path)) return descriptor.getService();
            }

            loaded = load(request, path);

            if(loaded == null && path.contains("/")) 
            {
                if(path.endsWith("/")) path = path.substring(0, path.lastIndexOf('/'));
                else path = path.substring(0, path.lastIndexOf('/')+1);
            }
        }
        while(loaded == null && path.length() > 0 && path.contains("/"));

        if(loaded == null) return null;

        return loaded.getService();
    }
    
    /**
     * Returns a {@link Service} for the {@link javax.servlet.http.HttpServletRequest}
     * 
     * @param request The request.
     * @return The {@link Service}.
     */    
    public Service get(javax.servlet.http.HttpServletRequest request)
    {
        return get(request, request.getRequestURI());
    }
    
    private class ServiceDescriptor
    {
        long timestamp;
        File file;
        String path;
        
        private Service service;

        public ServiceDescriptor(File file, String path)
        {
            this.file = file;
            this.path = path;
        }
        
        public Service getService()
        {
            if(this.service == null || this.timestamp < file.lastModified())
            {
                this.timestamp = file.lastModified();
                this.service = (Service)new XMLWiringParser(new FileSource(file)).getRoot();
            }
            
            return this.service;
        }
    }
}
