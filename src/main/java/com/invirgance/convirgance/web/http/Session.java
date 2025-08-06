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
package com.invirgance.convirgance.web.http;

import com.invirgance.convirgance.ConvirganceException;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author jbanes
 */
public class Session
{
    private Object session;

    public Session(Object session)
    {
        this.session = session;
    }
    
    private Object execSessionMethod(String methodName, Object... parameters)
    {
        Class clazz = session.getClass();
        Class[] types = new Class[parameters.length];
        
        for(int i=0; i<parameters.length; i++) types[i] = parameters[i].getClass();
        
        try
        {
            return clazz.getMethod(methodName, types).invoke(session, parameters);
        }
        catch(Exception e) { throw new ConvirganceException(e); }
    }
    
    public Object getAttribute(String name)
    {
        return execSessionMethod("getAttribute");
    }
    
    public void setAttribute(String name, Object value)
    {
        execSessionMethod("setAttribute", name, value);
    }
    
    public Iterable<String> getAttributeNames()
    {
        Enumeration<String> enumeration = (Enumeration<String>)execSessionMethod("getAttributeNames");
        
        return Collections.list(enumeration);
    }
}
