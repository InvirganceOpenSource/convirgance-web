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

import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 *
 * @author jbanes
 */
@Wiring
public class JSONArrayRequestParameter implements Parameter
{
    private String name;
    private String[] keys;

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getKeys()
    {
        return keys;
    }

    public void setKeys(String[] keys)
    {
        this.keys = keys;
    }

    @Override
    public Object getValue(HttpRequest request)
    {
        JSONArray<JSONObject> records = new JSONArray<>();
        String[] values;
        
        for(String key : keys)
        {
            values = request.getParameterValues(key);
            
            if(values == null) continue;
            
            for(int i=0; i<values.length; i++)
            {
                if(records.size() <= i) records.add(new JSONObject());
                
                records.get(i).put(key, values[i]);
            }
        }
        
        return records;
    }
}
