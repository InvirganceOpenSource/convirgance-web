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

// JavaDoc imports
import com.invirgance.convirgance.web.consumer.QueryConsumer;
import com.invirgance.convirgance.web.service.UpdateService;

/**
 * A mechanism for converting multiple HTML form input elements with the same name
 * into an array of objects that can be processed by Convirgance.
 * <br>
 * <br>
 * For example, a sequence of suggested scheduling dates might look like this:<br>
 * <pre><code>
 * &lt;div>
 *     &lt;input type="date" name="available_date" value="2025-02-10">
 *     &lt;select name="time_of_day">
 *         &lt;option selected>Morning&lt;/option>
 *         &lt;option>Afternoon&lt;/option>
 *         &lt;option>Evening&lt;/option>
 *     &lt;/select>
 * &lt;/div>
 * &lt;div>
 *     &lt;input type="date" name="available_date" value="2025-02-12">
 *     &lt;select name="time_of_day">
 *         &lt;option>Morning&lt;/option>
 *         &lt;option>Afternoon&lt;/option>
 *         &lt;option selected>Evening&lt;/option>
 *     &lt;/select>
 * &lt;/div>
 * </code></pre>
 * The submitted form would contain the values in order, like this:<br>
 * <pre><code>
 * available_date=2025-02-10&amp;time_of_day=Morning&amp;available_date=2025-02-12&amp;time_of_day=Evening
 * </code></pre>
 * Using this <code>Parameter</code> with the <code>available_date</code>
 * and <code>time_of_day</code> keys like this:<br>
 * <pre><code>
 * &lt;JSONArrayRequestParameter>
 *     &lt;name>fields&lt;/name>
 *     &lt;keys>available_date,time_of_day&lt;/keys>
 * &lt;/JSONArrayRequestParameter>
 * </code></pre>
 * ...results in the following value being returned:<br>
 * <pre><code>
 * [
 *     {"available_date": "2025-02-10", "time_of_day": "Morning"},
 *     {"available_date": "2025-02-12", "time_of_day": "Evening"}
 * ]
 * </code></pre>
 * This list of records could then be processed using the &lt;children> feature
 * in the {@link QueryConsumer} or {@link UpdateService} features.
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

    /**
     * Sets the name of the parameter to return
     * 
     * @param name the name of the parameter
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the array of parameter names that will be read into the array
     * 
     * @return an array of parameter names
     */
    public String[] getKeys()
    {
        return keys;
    }

    /**
     * Set a list of request parameter names that will be included in the array.
     * Note that the parameter names must appear the same number of times in the
     * correct order to ensure that the values are correctly decoded into the 
     * correct object in the {@link JSONArray}.
     * 
     * @param keys 
     */
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
