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
import java.io.InputStream;

/**
 * Provides direct access to the HTTP request body.
 * RequestBodyOrigin creates a Source that wraps the input stream of the HTTP request,
 * allowing direct access to the raw request body content.
 * 
 * <pre>
 * Use this origin when you need to:
 * - Process JSON or XML data sent in request bodies
 * - Handle file uploads or binary content
 * - Create REST API endpoints for data submission
 * </pre>
 * 
 * @author jbanes
 */
public class RequestBodyOrigin implements Origin
{
    /**
     * Gets a {@link Source} to the request's {@link InputStream}.
     * 
     * @param request The {@link HttpRequest}
     * @param parameters _unused.
     * @return A source to the requests input stream.
     */
    @Override
    public Source getOrigin(HttpRequest request, JSONObject parameters)
    {
        return new Source() {
            
            boolean used;
            
            @Override
            public InputStream getInputStream()
            {
                used = true;
                
                return request.getInputStream();
            }

            @Override
            public boolean isReusable()
            {
                return false;
            }

            @Override
            public boolean isUsed()
            {
                return used;
            }
        };
    }
}
