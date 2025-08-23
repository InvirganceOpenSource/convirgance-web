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
 * A meta-service that routes requests based on the HTTP Method. If a service
 * is not set for a particular method, a 404 Not Found error code will be 
 * returned. This router is intended for implementing the REST pattern where
 * GET obtains a record, POST inserts the record, PUT updates the record, and
 * DELETE removes the record.
 * 
 * @author jbanes
 */
@Wiring
public class RESTService implements Service, Routable
{
    private Service get;
    private Service post;
    private Service put;
    private Service delete;

    /**
     * The service handling the GET request
     * 
     * @return the service handling the GET request, null if no service set
     */
    public Service getGET()
    {
        return get;
    }

    /**
     * Set the service to handle GET requests
     * 
     * @param get the service to handle GET
     */
    public void setGET(Service get)
    {
        this.get = get;
    }

    /**
     * The service handling the POST request
     * 
     * @return the service handling the POST request, null if no service set
     */
    public Service getPOST()
    {
        return post;
    }

    /**
     * Set the service to handle POST requests
     * 
     * @param post the service to handle POST
     */
    public void setPOST(Service post)
    {
        this.post = post;
    }

    /**
     * The service handling the PUT request
     * 
     * @return the service handling the PUT request, null if no service set
     */
    public Service getPUT()
    {
        return put;
    }

    /**
     * Set the service to handle PUT requests
     * 
     * @param put the service to handle PUT
     */
    public void setPUT(Service put)
    {
        this.put = put;
    }

    /**
     * The service handling the DELETE request
     * 
     * @return the service handling the DEKETE request, null if no service set
     */
    public Service getDELETE()
    {
        return delete;
    }

    /**
     * Set the service to handle DELETE requests
     * 
     * @param delete the service to handle DELETE
     */
    public void setDELETE(Service delete)
    {
        this.delete = delete;
    }
    
    /**
     * Obtain the service handling the given method
     * 
     * @param method the HTTP method in uppercase characters
     * @return the service handling the given method or null if there is no handler
     */
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
    public Service getDestinationService(HttpRequest request)
    {
        return getService(request.getMethod());
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        Service service = getDestinationService(request);

        if(service != null) service.execute(request, response);
        else response.sendError(404, "Not Found");
    }
    
}
