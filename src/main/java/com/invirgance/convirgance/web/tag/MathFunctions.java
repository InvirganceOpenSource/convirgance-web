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

/**
 *
 * @author jbanes
 */
public class MathFunctions
{
    
    public static Number abs(Number value)
    {
        if(value == null) return null;
        if(value instanceof Float) return Math.abs((Float)value);
        if(value instanceof Double) return Math.abs((Double)value);
        if(value instanceof Integer) return Math.abs((Integer)value);
        if(value instanceof Long) return Math.abs((Long)value);
        
        return Math.abs(value.doubleValue());
    }
    
    public static Number min(Number left, Number right)
    {
        if(left == null) return right;
        if(right == null) return left;
        
        if(left instanceof Float && right instanceof Float) return Math.min((Float)left, (Float)right);
        if(left instanceof Double && right instanceof Double) return Math.min((Double)left, (Double)right);
        if(left instanceof Integer && right instanceof Integer) return Math.min((Integer)left, (Integer)right);
        if(left instanceof Long && right instanceof Long) return Math.min((Long)left, (Long)right);
        
        return Math.min(left.doubleValue(), right.doubleValue());
    }
    
    public static Number max(Number left, Number right)
    {
        if(left == null) return right;
        if(right == null) return left;
        
        if(left instanceof Float && right instanceof Float) return Math.max((Float)left, (Float)right);
        if(left instanceof Double && right instanceof Double) return Math.max((Double)left, (Double)right);
        if(left instanceof Integer && right instanceof Integer) return Math.max((Integer)left, (Integer)right);
        if(left instanceof Long && right instanceof Long) return Math.max((Long)left, (Long)right);
        
        return Math.max(left.doubleValue(), right.doubleValue());
    }
    
    public static Number floor(Number value)
    {
        if(value == null) return null;
        
        return Math.floor(value.doubleValue());
    }
    
    public static Number ceil(Number value)
    {
        if(value == null) return null;
        
        return Math.ceil(value.doubleValue());
    }
    
    public static Number round(Number value)
    {
        if(value == null) return null;
        
        return Math.round(value.doubleValue());
    }
}
