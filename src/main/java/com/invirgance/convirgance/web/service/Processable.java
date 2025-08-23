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
import com.invirgance.convirgance.web.http.HttpRequest;

/**
 * Implemented by services that can be called directly to return a stream of
 * data without generating an HTTP response. Primarily used internally to allow
 * internal service calls to work. (e.g. <code>virge:service</code> tag and 
 * <code>HypermediaVerb</code> service calls.
 * 
 * @author jbanes
 */
public interface Processable
{
    /**
     * Processes an HTTP request to retrieve data without generating a response.
     * <p>
     * This method implements the core data retrieval pipeline and can be used
     * by other components that need access to the data without writing to a response.
     * </p>
     * <ol>
     *   <li>Extract parameters from the HTTP request</li>
     *   <li>Store parameters in thread-local storage for access by other components</li>
     *   <li>Use the configured binding to retrieve data based on the parameters</li>
     *   <li>Apply any configured transformers to the data</li>
     * </ol>
     *
     * @param request The HTTP request containing the parameters
     * @return An iterable of JSONObjects containing the retrieved and transformed data
     */
    public Iterable<JSONObject> process(HttpRequest request);
}
