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

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jbanes
 */
public class JakartaParameterizedRequest extends HttpServletRequestWrapper
{
    private HttpServletRequest request;
    private JSONObject parameters;
    private String path;
    private String method;
    private byte[] input;
    
    public JakartaParameterizedRequest(Object request, JSONObject parameters)
    {
        this(request, parameters, null);
    }
    
    public JakartaParameterizedRequest(Object request, JSONObject parameters, String path)
    {
        this(request, parameters, path, null);
    }
    
    public JakartaParameterizedRequest(Object request, JSONObject parameters, String path, String method)
    {
        this(request, parameters, path, method, null);
    }

    public JakartaParameterizedRequest(Object request, JSONObject parameters, String path, String method, JSONObject data)
    {
        super((HttpServletRequest)request);
        
        this.request = (HttpServletRequest)request;
        this.parameters = parameters;
        this.path = path;
        this.method = (method == null) ? this.request.getMethod() : method;
        
        if(data != null)
        {
            try { this.input = data.toString().getBytes("UTF-8"); } catch(Exception e) { throw new ConvirganceException(e); }
        }
    }

    @Override
    public String getMethod()
    {
        return method;
    }

    @Override
    public String getParameter(String name)
    {
        if(parameters.containsKey(name)) return parameters.getString(name);
        
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name)
    {
        JSONArray values;
        String[] result;
        int index = 0;
        
        if(!parameters.containsKey(name)) return super.getParameterValues(name);
        
        if(parameters.get(name) instanceof JSONArray)
        {
            values = parameters.getJSONArray(name);
            result = new String[values.size()];
            
            for(Object value : values) result[index++] = String.valueOf(value);
            
            return result;
        }
        
        return new String[]{ getParameter(name) };
    }
    
    private List<String> getParameterNameList()
    {
        var list = new ArrayList<String>(parameters.keySet());
        var enumeration = super.getParameterNames();
        
        String name;
        
        while(enumeration.hasMoreElements())
        {
            name = enumeration.nextElement();
            
            if(!list.contains(name)) list.add(name);
        }
        
        return list;
    }
    
    @Override
    public Enumeration<String> getParameterNames()
    {
        return Collections.enumeration(getParameterNameList());
    }

    @Override
    public Map<String,String[]> getParameterMap()
    {
        Map map = new JSONObject(true);
        
        for(String name : getParameterNameList())
        {
            map.put(name, getParameterValues(name));
        }
        
        return map;
    }

    @Override
    public String getRequestURI()
    {
        var path = this.path;
        
        if(path == null) return super.getRequestURI();
        if(!path.startsWith("/")) path = "/" + path;
        
        return getContextPath() + path;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        if(input == null) return super.getInputStream();
        
        return new ServletInputStreamWrapper();
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        if(input == null) return super.getReader();
        
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
        
    private class ServletInputStreamWrapper extends ServletInputStream
    {
        private int offset = 0;
        
        @Override
        public boolean isFinished()
        {
            return (offset < input.length);
        }

        @Override
        public boolean isReady()
        {
            return !isFinished();
        }

        @Override
        public void setReadListener(ReadListener rl)
        {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int read() throws IOException
        {
            if(offset >= input.length) return -1;
            
            return input[offset++];
        }

        @Override
        public int read(byte[] b) throws IOException
        {
            return read(b, 0, b.length);
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException
        {
            int actual = Math.min(input.length - offset, len);
            
            if(offset >= input.length) return -1;

            System.arraycopy(input, offset, b, off, actual);
            
            offset += actual;
            
            return actual;
        }
    }
}
