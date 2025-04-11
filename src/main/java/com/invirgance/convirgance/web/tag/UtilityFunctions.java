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
package com.invirgance.convirgance.web.tag;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.json.JSONWriter;
import com.invirgance.convirgance.output.JSONOutput;
import com.invirgance.convirgance.target.ByteArrayTarget;
import jakarta.servlet.jsp.JspException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Utility functions for JSP tags, providing helpers for JSON conversion,
 * HTML escaping, URL encoding, and collection operations.
 * 
 * @author jbanes
 */
public class UtilityFunctions
{
    
    /**
     * Converts a JSONObject iterable to a JSON string.
     * 
     * @param iterable An iterable of JSONObjects
     * @return A JSON string representation
     * @throws JspException If there is an encoding issue
     */
    public static String json(Iterable<JSONObject> iterable) throws JspException
    {
        ByteArrayTarget target = new ByteArrayTarget();
        JSONOutput output = new JSONOutput();
        
        output.write(target, iterable);
        
        try
        {
            return new String(target.getBytes(), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Renders out a Object as JSON to a string.
     * 
     * @param value The object.
     * @return The string.
     * @throws JspException If an encoding issues occurs.
     */
    public static String json(Object value) throws JspException
    {
        if(value instanceof Iterable) return json((Iterable<JSONObject>)value);
        
        try
        {
            return new JSONWriter().write(value).toString();
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Escapes a value for safe HTML output, replacing special characters 
     * with their HTML entity equivalents.
     * 
     * @param value The value to make HTML safe
     * @return A string with HTML special characters escaped
     */
    public static String html(Object value) 
    {
        return String.valueOf(value)
                     .replaceAll("&", "&amp;")
                     .replaceAll("<", "&lt;")
                     .replaceAll(">", "&gt;")
                     .replaceAll("\"", "&quot;")
                     .replaceAll("'", "&#39;");
    }
    
    /**
     * Makes the object URL safe.
     * 
     * @param value The value.
     * @return The URL encoded version.
     * @throws JspException If an encoding issue occurs.
     */
    public static String urlparam(Object value) throws JspException
    {
        try
        {
            return URLEncoder.encode(String.valueOf(value), "UTF-8");
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
    }
    
    /**
     * Returns the first item in an iterable collection.
     * Handles closing the iterator if it implements AutoCloseable.
     * 
     * @param iterable The collection
     * @return The first item, or null if empty
     * @throws JspException If an error occurs while iterating or closing
     */
    public static Object first(Iterable iterable) throws JspException
    {
        Iterator<JSONObject> iterator = iterable.iterator();
        Object record = iterator.hasNext() ? iterator.next() : null;
        
        if(iterator instanceof AutoCloseable)
        {
            try { ((AutoCloseable)iterator).close(); } catch(Exception e) { throw new JspException(e); }
        }
        
        return record;
    }
    
    /**
     * Gets the last item in a iterable.
     * 
     * @param iterable The iterable.
     * @return The last item.
     * @throws JspException _unused
     */
    public static Object last(Iterable iterable) throws JspException
    {
        Iterator<JSONObject> iterator = iterable.iterator();
        Object record = null;
        
        while(iterator.hasNext()) record = iterator.next();
        
        return record;
    }
}
