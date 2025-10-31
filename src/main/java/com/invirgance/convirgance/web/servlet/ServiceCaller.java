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
package com.invirgance.convirgance.web.servlet;

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.service.Processable;
import com.invirgance.convirgance.web.service.Routable;
import com.invirgance.convirgance.web.service.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for invoking {@link Processable} services and returning
 * their results as {@link JSONObject}s.
 * 
 * @author jbanes
 */
public class ServiceCaller
{
    /**
     * Uses the service at path, processing the request and returning data.
     * The service will utilize any relevant request parameters.
     * 
     * @param request A {@link HttpServletRequest}
     * @param path A path to a service.
     * @param parameters The requests parameters that will be used by the service.
     * @return An iterable response from the service.
     * @throws IllegalArgumentException if a select service doesn't exist for the path.
     */
    public static Iterable<JSONObject> select(HttpServletRequest request, String path, JSONObject parameters)
    {
        ServiceLoader loader = ServiceLoader.getInstance();
        Service service = loader.get(request, path);
        ParameterizedHttpRequest wrapper = new ParameterizedHttpRequest(request, path, parameters);
        
        while(service instanceof Routable) service = ((Routable)service).getDestinationService(wrapper);

        if(service == null) throw new IllegalArgumentException(path + " is not found.");
        if(!(service instanceof Processable)) throw new IllegalArgumentException(path + " does not implement Processable and thus cannot return data.");
        
        return ((Processable)service).process(wrapper);
    }
    
    private static class ParameterizedHttpRequest extends HttpRequest
    {
        private JSONObject parameters;
        private String path;

        public ParameterizedHttpRequest(HttpServletRequest request, String path, JSONObject parameters)
        {
            super(request);
            
            this.path = path;
            this.parameters = parameters;
            
            if(!path.startsWith("/")) this.path = "/" + path;
        }

        @Override
        public String getParameter(String name)
        {
            return parameters.getString(name);
        }

        @Override
        public Map<String, String[]> getParameterMap()
        {
            HashMap<String, String[]> map = new HashMap<>();
            Object value;
            
            for(String key : this.parameters.keySet())
            {
                value = this.parameters.get(key);
                
                if(value instanceof String[]) map.put(key, (String[])value);
                else if(value instanceof List) map.put(key, ((List<String>)value).toArray(String[]::new));
                else map.put(key, new String[]{ value.toString() });
            }
            
            return map;
        }

        @Override
        public Iterable<String> getParameterNames()
        {
            return this.parameters.keySet();
        }

        @Override
        public String[] getParameterValues(String name)
        {
            JSONArray<String> array = new JSONArray<>(this.parameters.values());
            
            return array.toArray(String[]::new);
        }

        @Override
        public String getRequestURI()
        {
            return super.getContextPath() + path;
        }
    }
}
