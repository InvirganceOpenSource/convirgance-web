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
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 *
 * @author jbanes
 */
public class RegExValidation implements Validation
{
    private String key;
    private Pattern regex;
    private Predicate<String> predicate;

    public RegExValidation()
    {
    }

    public RegExValidation(String key, String regex)
    {
        this.key = key;
        this.regex = Pattern.compile(regex);
        this.predicate = this.regex.asMatchPredicate();
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getRegex()
    {
        if(regex == null) return null;
        
        return regex.pattern();
    }

    public void setRegex(String regex)
    {
        this.regex = Pattern.compile(regex);
        this.predicate = this.regex.asMatchPredicate();
    }

    @Override
    public void validate(JSONObject record) throws ValidationException
    {
        String value = record.getString(key);
        
        // We dont' attempt to validate null values
        if(value == null) return;

        if(!predicate.test(value))
        {
            throw new ValidationException(key + " does not match pattern " + regex.pattern() + "! Record:\n" + record.toString(4));
        }
    }
}
