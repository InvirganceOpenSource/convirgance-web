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
package com.invirgance.convirgance.web.parameter;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Extracts a variable from the URL path. This parameter can only extract a 
 * single parameter, so multiple parameters will need to be configured to
 * extract multiple values. Parameter names are identified in the path with
 * curly braces. e.g. <code>/path/{id}</code> defines a variable called
 * <code>id</code> that can be extracted from the path.<br>
 * <br>
 * A <code>*</code> can be used whenever a dynamic path component needs to be
 * defined but not extracted by this parameter. e.g. <code>/path/%2A/subpath/{id}</code>
 * defines a dynamic path component between <code>path</code> and <code>subpath</code>.
 * 
 * @author jbanes
 */
@Wiring
public class PathVariable implements Parameter
{
    private String path;

    /**
     * The path pattern that has been set
     * 
     * @return the parameterized path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set the path pattern
     * 
     * @param path the parameterized path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public String getName()
    {
        int start = path.indexOf('{');
        int end = path.indexOf('}', Math.max(start, 0));
        
        if(start < 0) throw new ConvirganceException("Path " + path + " does not contain any variables");
        if(end < 0) throw new ConvirganceException("Path " + path + " does not have a terminating '}'");
        if(path.indexOf('{', start+1) > 0) throw new ConvirganceException("Path " + path + " has more than one defined variable. Use a * for unknown parts of the path.");
        
        return path.substring(start+1, end);
    }
    
    private String normalize(String actual, String component)
    {
        String left = component.substring(0, component.indexOf('{'));
        String right = component.substring(component.indexOf('}')+1);
        
        if(!actual.startsWith(left)) return null;
        if(!actual.endsWith(right)) return null;
        
        return actual.substring(component.indexOf('{'), actual.length() - right.length());
    }
    
    private String trimContext(String path, String context)
    {
        if(context == null) return path;
        
        if(path.startsWith(context)) return path.substring(context.length());
        
        return path;
    }

    @Override
    public Object getValue(HttpRequest request)
    {
        String name = getName();
        String[] actual = trimContext(request.getRequestURI(), request.getContextPath()).split("/");
        String[] components = trimContext(this.path, request.getContextPath()).split("/");
        
        int length = Math.min(actual.length, components.length);
        String token = "{" + name + "}";
        
        for(int i=0; i<length; i++)
        {
            if(components[i].equals("*")) continue;
            if(components[i].contains(token)) return normalize(actual[i], components[i]);
            if(!actual[i].equals(components[i])) return null;
        }
        
        return null;
    }
}
