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

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.json.JSONWriter;
import com.invirgance.convirgance.output.JSONOutput;
import com.invirgance.convirgance.target.ByteArrayTarget;
import jakarta.servlet.jsp.JspException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author jbanes
 */
public class UtilityFunctions
{
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
}
