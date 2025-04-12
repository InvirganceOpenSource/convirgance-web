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

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.transform.Transformer;
import java.util.Iterator;

/**
 * Validations are transformers that ensure correctness of a stream of data and
 * cancel the stream if a problem is found.
 * 
 * @author jbanes
 */
public interface Validation extends Transformer
{
    @Override
    public default Iterator<JSONObject> transform(Iterator<JSONObject> iterator) throws ConvirganceException
    {
        return validate(iterator);
    }
    
    public default Iterator<JSONObject> validate(Iterator<JSONObject> iterator)
    {
        return new Iterator<JSONObject>() {
            
            private JSONObject next;
            
            /**
             * Checks if there is a next record that passes the filter
             * condition. Iterates over the source iterator and finds the next
             * record that passes the filter.
             *
             * @return `true` if a valid record is found, `false` otherwise.
             */
            @Override
            public boolean hasNext()
            {
                JSONObject next = this.next;
                
                if(next != null) return true;
                
                while(iterator.hasNext() && this.next == null)
                {
                    next = iterator.next();
                    
                    validate(next);
                    
                    this.next = next;
                }
                
                return (this.next != null);
            }

            /**
             * Returns the next valid record that passes the filter condition.
             * Calls `hasNext()` to ensure the record is valid before returning.
             *
             * @return The next valid JSONObject.
             */
            @Override
            public JSONObject next()
            {
                JSONObject next = this.next;
                
                if(next == null) 
                {
                    hasNext();
                    next = this.next;
                }
                
                this.next = null;
                
                return next;
            }
        };
    }
    
    /**
     * Tests if the given record is valid
     *
     * @param record The JSONObject to validate.
     * @throws ValidationException if the record is invalid
     */
    public void validate(JSONObject record) throws ValidationException;
}
