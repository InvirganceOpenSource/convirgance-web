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
package com.invirgance.convirgance.web.validation;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Checks the minimum number of characters and maximum number of characters in a string
 * 
 * @author jbanes
 */
@Wiring
public class SizeValidation implements Validation
{
    public static final int NO_MIN = 0;
    public static final int NO_MAX = 0;
    
    private String key;
    private int min;
    private int max;

    public SizeValidation()
    {
    }

    public SizeValidation(String key, int min, int max)
    {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public int getMin()
    {
        return min;
    }

    public void setMin(int min)
    {
        this.min = min;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    @Override
    public void validate(JSONObject record) throws ValidationException
    {
        String value = record.getString(key).trim();
        
        if(value == null) return;
        if(min > 0 && value.length() < min) throw new ValidationException(value.length() + " is too few characters for " + key + " which requires a minimum of " + min + " characters. Record:\n" + record);
        if(max > 0 && value.length() > max) throw new ValidationException(value.length() + " is too many characters for " + key + " which requires a maximum of " + max + " characters. Record:\n" + record);
    }
    
}
