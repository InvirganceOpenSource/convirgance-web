/*
 * Copyright 2024 INVIRGANCE LLC

Permission is hereby granted, free of charge, to any person obtaining a copy 
of this software and associated documentation files (the “Software”), to deal 
in the Software without restriction, including without limitation the rights to 
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.
 */
package com.invirgance.convirgance.web.service;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.filter.CoerciveComparator;

/**
 *
 * @author jbanes
 */
public class BindingParameter implements Comparable
{
    static final CoerciveComparator comparator = new CoerciveComparator();
    static final InheritableThreadLocal<JSONObject> bindings = new InheritableThreadLocal<>();
    
    private String key;

    public BindingParameter()
    {
    }

    public BindingParameter(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
    
    public Object getValue()
    {
        return bindings.get().get(key);
    }

    @Override
    public int hashCode()
    {
        return getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return getValue().equals(obj);
    }

    @Override
    public String toString()
    {
        return getValue().toString();
    }

    @Override
    public int compareTo(Object object)
    {
        return comparator.compare(this, object);
    }
}
