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

import com.invirgance.convirgance.web.service.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author jbanes
 */
class ServiceLoader
{
    private static ServiceLoader instance;
    
    private List<ServiceDescriptor> cache = new ArrayList<>();
    
    public static ServiceLoader getInstance()
    {
        if(instance == null) instance = new ServiceLoader();
        
        return instance;
    }
    
    public ServiceDescriptor load(jakarta.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor descriptor;
        String context = request.getContextPath();
        String filePath;
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else if(!path.endsWith(".xml")) path += ".xml";

        // Transform URI path to file path
        filePath = request.getServletContext().getRealPath(path);

        if(filePath == null || !new File(filePath).exists()) return null;
        
        descriptor = new ServiceDescriptor(new File(filePath), path);
        
        this.cache.add(descriptor);
        
        return descriptor;
    }
    
    public ServiceDescriptor load(jakarta.servlet.http.HttpServletRequest request)
    {
        ServiceDescriptor descriptor;
        String path = request.getRequestURI();
        String context = request.getContextPath();
        String filePath;
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        // Transform URI path to file path
        filePath = request.getServletContext().getRealPath(path);

        if(filePath == null || !new File(filePath).exists()) return null;
        
        descriptor = new ServiceDescriptor(new File(filePath), path);
        
        this.cache.add(descriptor);
        
        return descriptor;
    }
    
    public Service get(jakarta.servlet.http.HttpServletRequest request, String path)
    {
        ServiceDescriptor loaded;
        String context = request.getContextPath();
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        for(ServiceDescriptor descriptor : this.cache)
        {
            if(descriptor.path.equals(path)) return descriptor.getService();
        }
        
        loaded = load(request, path);

        if(loaded == null) return null;

        return loaded.getService();
    }
    
    public Service get(jakarta.servlet.http.HttpServletRequest request)
    {
        ServiceDescriptor loaded;
        String path = request.getRequestURI();
        String context = request.getContextPath();
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        for(ServiceDescriptor descriptor : this.cache)
        {
            if(descriptor.path.equals(path)) return descriptor.getService();
        }
        
        loaded = load(request);

        if(loaded == null) return null;

        return loaded.getService();
    }
    
    public ServiceDescriptor load(javax.servlet.http.HttpServletRequest request)
    {
        ServiceDescriptor descriptor;
        String path = request.getRequestURI();
        String context = request.getContextPath();
        String filePath;
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        // Transform URI path to file path
        filePath = request.getServletContext().getRealPath(path);

        if(filePath == null || !new File(filePath).exists()) return null;
        
        descriptor = new ServiceDescriptor(new File(filePath), path);
        
        this.cache.add(descriptor);
        
        return descriptor;
    }
    
    public Service get(javax.servlet.http.HttpServletRequest request)
    {
        ServiceDescriptor loaded;
        String path = request.getRequestURI();
        String context = request.getContextPath();
        
        if(path.startsWith(context)) path = path.substring(context.length());
        
        if(path.endsWith("/")) path += "spring.xml";
        else path += ".xml";
        
        for(ServiceDescriptor descriptor : this.cache)
        {
            if(descriptor.path.equals(path)) return descriptor.getService();
        }
        
        loaded = load(request);

        if(loaded == null) return null;

        return loaded.getService();
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
                this.service = new GenericXmlApplicationContext(new FileSystemResource(file)).getBean(Service.class);
            }
            
            return this.service;
        }
    }
}
