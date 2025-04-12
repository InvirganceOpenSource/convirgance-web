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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class SizeValidationTest
{
    @Test
    public void testMin()
    {
        JSONObject record = new JSONObject("{\"x\": 0, \"y\": 10, \"z\": \"0123456789\"}");
        
        try
        {
            new SizeValidation("x", 2, 0).validate(record);
            
            fail("Should have failed minimum validation");
        }
        catch(ValidationException e)
        {
            assertTrue(e.getMessage().startsWith("1 is too few characters for x which requires a minimum of 2 characters."));
        }
        
        new SizeValidation("y", 2, 0).validate(record);
        new SizeValidation("z", 2, 0).validate(record);
    }
    
    @Test
    public void testMax()
    {
        JSONObject record = new JSONObject("{\"x\": 0, \"y\": 10, \"z\": \"0123456789\"}");
        
        new SizeValidation("x", 0, 4).validate(record);
        new SizeValidation("y", 0, 4).validate(record);
        
        try
        {
            new SizeValidation("z", 2, 4).validate(record);
            
            fail("Should have failed maximum validation");
        }
        catch(ValidationException e)
        {
            assertTrue(e.getMessage().startsWith("10 is too many characters for z which requires a maximum of 4 characters."));
        }
    }
    
    @Test
    public void testRange()
    {
        JSONObject record = new JSONObject("{\"x\": 0, \"y\": 10, \"z\": \"0123456789\"}");
        
        
        try
        {
            new SizeValidation("x", 2, 0).validate(record);
            
            fail("Should have failed minimum validation");
        }
        catch(ValidationException e)
        {
            assertTrue(e.getMessage().startsWith("1 is too few characters for x which requires a minimum of 2 characters."));
        }
        
        new SizeValidation("y", 2, 4).validate(record);
        
        try
        {
            new SizeValidation("z", 2, 4).validate(record);
            
            fail("Should have failed maximum validation");
        }
        catch(ValidationException e)
        {
            assertTrue(e.getMessage().startsWith("10 is too many characters for z which requires a maximum of 4 characters."));
        }
    }
    
}
