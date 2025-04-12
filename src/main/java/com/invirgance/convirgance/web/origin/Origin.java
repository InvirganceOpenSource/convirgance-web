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
package com.invirgance.convirgance.web.origin;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.source.Source;
import com.invirgance.convirgance.web.http.HttpRequest;

/**
 * Extract raw data from HTTP requests in various ways and provide it as a 
 * Source that can be processed.
 * 
 * @author jbanes
 */
public interface Origin
{
    /**
     * Extracts data from the HTTP request and returns it as a Source.
     * 
     * @param request The HTTP request to extract data from
     * @param parameters Additional parameters that may influence extraction
     * @return A Source providing access to the extracted data
     */
    public Source getOrigin(HttpRequest request, JSONObject parameters);
}
