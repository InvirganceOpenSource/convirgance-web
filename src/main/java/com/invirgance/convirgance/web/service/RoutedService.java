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
package com.invirgance.convirgance.web.service;

import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jbanes
 */
public class RoutedService implements Service, Routable
{
    private Map<String,Service> routes;
    private List<String> sortedKeys;

    public Map<String,Service> getRoutes()
    {
        return routes;
    }

    public void setRoutes(Map<String, Service> routes)
    {
        this.routes = routes;
        
        // Ensure the keys are sorted to check longer paths first
        if(routes == null) sortedKeys = null;
        else sortedKeys = new ArrayList<>(routes.keySet());
    }
    
    private String trimContext(String path, String context)
    {
        if(context == null) return path;
        
        if(path.startsWith(context)) return path.substring(context.length());
        
        return path;
    }
    
    @Override
    public Service getDestinationService(HttpRequest request)
    {
        String[] actual = trimContext(request.getRequestURI(), request.getContextPath()).split("/");
        String[] components;
        
        routes: for(String path : sortedKeys)
        {
            components = trimContext(path, request.getContextPath()).split("/");

            if(actual.length < components.length) continue;
            
            for(int i=0; i<components.length; i++)
            {
                if(components[i].equals("*")) continue;
                if(!actual[i].equals(components[i])) continue routes;
            }

            return routes.get(path);
        }
        
        return null;
    }
    
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        Service service = getDestinationService(request);
        
        if(service != null) service.execute(request, response);
        else response.sendError(404, "Not Found");
    }
    
}
