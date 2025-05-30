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

import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;

/**
 * Core interface for all web service implementations in the framework.
 * 
 * <pre>
 * Primary implementations include:
 * - {@link SelectService}: Retrieves and returns data (GET operations)
 * - {@link InsertService}: Accepts and persists data (POST operations)
 * </pre>
 * 
 * @author jbanes
 */
public interface Service
{
    /**
     * Executes the service, processing the HTTP request and generating a response.
     *
     * @param request The HTTP request to process
     * @param response The HTTP response to populate
     */    
    public void execute(HttpRequest request, HttpResponse response);
}
