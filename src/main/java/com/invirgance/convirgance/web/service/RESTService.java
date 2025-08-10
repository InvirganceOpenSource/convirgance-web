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

import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 *
 * @author jbanes
 */
@Wiring
public class RESTService implements Service
{
    private Service get;
    private Service post;
    private Service put;
    private Service delete;

    public Service getGET()
    {
        return get;
    }

    public void setGET(Service get)
    {
        this.get = get;
    }

    public Service getPOST()
    {
        return post;
    }

    public void setPOST(Service post)
    {
        this.post = post;
    }

    public Service getPUT()
    {
        return put;
    }

    public void setPUT(Service put)
    {
        this.put = put;
    }

    public Service getDELETE()
    {
        return delete;
    }

    public void setDELETE(Service delete)
    {
        this.delete = delete;
    }
    
    public Service getService(String method)
    {
        switch(method)
        {
            case "GET":
                return get;
                
            case "POST":
                return post;
                
            case "PUT":
                return put;
                
            case "DELETE":
                return delete;
                
            default:
                return null;
        }
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        switch(request.getMethod())
        {
            case "GET":
                if(get != null) get.execute(request, response);
                else response.sendError(404, "Not Found");
                break;
                
            case "POST":
                if(post != null) post.execute(request, response);
                else response.sendError(404, "Not Found");
                break;
                
            case "PUT":
                if(put != null) put.execute(request, response);
                else response.sendError(404, "Not Found");
                break;
                
            case "DELETE":
                if(delete != null) delete.execute(request, response);
                else response.sendError(404, "Not Found");
                break;
                
            default:
                response.sendError(404, "Not Found");
                break;
        }
    }
    
}
