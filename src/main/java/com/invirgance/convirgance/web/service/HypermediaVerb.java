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

import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * Descriptor for handling Hypermedia REST requests using only GET and POST. A
 * verb may provide a page to render for GET requests and a service to execute
 * for POST requests. After the service is called, the client is redirected to
 * a path without the verb by default. e.g. <code>/owner/10/edit</code> will 
 * redirect to <code>/owner/10</code>.<br>
 * <br>
 * If an alternate redirect is desired, a redirect path can be set. The redirect
 * path may contain parameters collected by the Hypermedia service or returned
 * by the service configured to handle the POST request.
 * 
 * @author jbanes
 */
@Wiring("verb")
public class HypermediaVerb
{
    private String name;
    private String page;
    private String service;
    private String method;
    private String redirect;

    /**
     * The name of the verb to handle. e.g. "create", "edit", "add". Verbs are 
     * case-sensitive and are always the last component of the URL path.
     * 
     * @return the name of the verb
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the verb to handle. e.g. "create", "edit", "add". Verbs are 
     * case-sensitive and are always the last component of the URL path.
     * 
     * @param name the name this verb is to handle
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Path to a JSP page or Servlet to handle GET requests on the verb
     * 
     * @return path to a JSP page or Servlet
     */
    public String getPage()
    {
        return page;
    }

    /**
     * Set the path to a JSP page or Servlet to handle GET requests on the verb
     * 
     * @param page path to a JSP page or Servlet
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    /**
     * Path to a service for handling POST requests
     * 
     * @return path to the service
     */
    public String getService()
    {
        return service;
    }

    /**
     * Set path to a service for handling POST requests
     * 
     * @param service path to the service
     */
    public void setService(String service)
    {
        this.service = service;
    }

    /**
     * HTTP method to use when calling the service for handling POST requests
     * 
     * @return HTTP method or null if the method used to trigger the verb is to be passed through 
     */
    public String getMethod()
    {
        return method;
    }

    /**
     * Set HTTP method to use when calling the service for handling POST requests. Method
     * must be upper case. e.g. POST, PUT, DELETE
     * 
     * @param method upper case HTTP method 
     */
    public void setMethod(String method)
    {
        this.method = method;
    }

    /**
     * If set, overrides the default redirect path after a POST request. 
     * 
     * @return redirect path if set, null otherwise
     */
    public String getRedirect()
    {
        return redirect;
    }

    /**
     * Sets the redirect path to use after a POST request. Paths can contain
     * parameters collected by the Hypermedia service or returned by the service
     * called to handle the POST. e.g. <code>/owner/{ownerId}</code>
     * 
     * @param redirect path to redirect to after a POST request
     */
    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }
}
