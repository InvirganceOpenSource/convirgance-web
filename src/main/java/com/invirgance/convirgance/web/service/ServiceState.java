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

import com.invirgance.convirgance.json.JSONObject;

/**
 * Records the current state of the Service using a ThreadLocal so that pluggable
 * objects can access the state during execution.
 * 
 * @author jbanes
 */
public class ServiceState
{
    private static final ThreadLocal<JSONObject> local = new ThreadLocal<>();
    
    /**
     * Get the key configured by the current Service. e.g. "parameters" is often
     * a JSONObject containing parameters collected from the request, session, and
     * other locations.
     * 
     * @param key the key to look up
     * @return the value of the key if found, null otherwise
     */
    public static Object get(String key)
    {
        JSONObject state = local.get();
        
        if(state == null) return null;
        
        return state.get(key);
    }
    
    /**
     * Sets the key/value pair on the thread local
     * 
     * @param key the key to set
     * @param value the value to set for the key
     */
    public static void set(String key, Object value)
    {
        JSONObject state = local.get();
        
        
        if(state == null)
        {
            state = new JSONObject();
            
            local.set(state);
        }
        
        state.put(key, value);
    }
    
    /**
     * Clears the thread local of information to prepare the thread for reuse,
     * prevent leaking of information, and avoid memory leaks
     */
    public static void release()
    {
        local.remove();
    }
}
