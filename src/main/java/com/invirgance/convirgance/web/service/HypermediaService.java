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

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.parameter.Parameter;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Provides a Hypermedia view for a resource, including handling of REST-like
 * calls routed through verbs to account for only GET/POST being supported by
 * HTML forms. Verbs are computed as the last component of the path and optionally
 * provide both a view and a service handler. POST requests to a verb will call
 * a configured service to handle the request and then redirect to a GET request to 
 * complete the Hypermedia rendering.<br>
 * <br>
 * Parameters configured by this service will be received by pages called as
 * regular HTTP request parameters. This allows configured parameters to be
 * accessed by JSP pages as if they were in the url query string. e.g. <code>${param.id}</code>,
 * &lt;%= request.getParameter("id") %&gt;, etc.
 * 
 * @author jbanes
 */
@Wiring
public class HypermediaService implements Service
{
    // TODO: Optional header/footer composition
    private List<Parameter> parameters;
    private List<HypermediaVerb> verbs;
    private String page;
    
    private Map<String,HypermediaVerb> lookup;

    /**
     * Gets the list of parameters to extract from the request.
     *
     * @return The parameters list
     */
    public List<Parameter> getParameters()
    {
        return parameters;
    }

    /**
     * Sets the list of parameters to extract from the request.
     *
     * @param parameters The parameters list
     */
    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    /**
     * The default JSP or Servlet path to render for GET requests. The page will
     * receive all parameters configured by this service as request parameters.
     * 
     * @return the path to the JSP or servlet or null if no page has been set
     */
    public String getPage()
    {
        return page;
    }

    /**
     * Set a path to the default JSP or Servlet to render for GET requests. The 
     * page will receive all parameters configured by this service as request 
     * parameters.
     * 
     * @param page the path to a JSP or servlet
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    /**
     * List of configured verbs for handling special requests like editing and
     * creating records.
     * 
     * @return list of configured verbs
     */
    public List<HypermediaVerb> getVerbs()
    {
        return verbs;
    }

    /**
     * Set a list of configured verbs for handling special requests like editing
     * and creating records.
     * 
     * @param verbs list of verbs
     */
    public void setVerbs(List<HypermediaVerb> verbs)
    {
        this.verbs = verbs;
        this.lookup = new HashMap<>();
        
        for(var verb : verbs) lookup.put(verb.getName(), verb);
    }
    
    private String constructPath(String path, JSONObject parameters)
    {
        StringBuffer buffer = new StringBuffer(path.length() * 2);
        String name;
        String value;
        int start;
        int end;
        
        for(var component : path.split("/"))
        {
            if(component.trim().length() < 1) continue;

            buffer.append('/');
            
            while((start = component.indexOf('{')) >= 0)
            {
                end = component.indexOf('}', start);
                
                if(end < 0) throw new ConvirganceException("Unbalanced curly braces in " + path);
                
                name = component.substring(start+1, end);
                value = parameters.getString(name);
                
                if(value == null) throw new ConvirganceException("Parameter " + name + " has not been configured and cannot be replaced in " + path);
                else component = component.replace("{" + name + "}", value);
            }
            
            buffer.append(component);
        }
        
        return buffer.toString();
    }
    
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        var parameters = new JSONObject();
        var path = request.getRequestURI();
        var method = request.getMethod().toUpperCase();

        var data = new JSONObject();
        var verb = path.substring(path.lastIndexOf('/')+1);
        var page = this.page;
        
        Iterable<JSONObject> results;
        Iterator<JSONObject> iterator;
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        if(this.lookup == null) this.lookup = new HashMap<>();
        
        // Obtain the parameters for binding
        for(Parameter parameter : this.parameters)
        {
            parameters.put(parameter.getName(), parameter.getValue(request));
        }
        
        if(method.equals("POST"))
        {
            if(!lookup.containsKey(verb)) throw new ConvirganceException("No service handler for verb /" + verb + " on path " + path);
            
            for(String name : request.getParameterNames()) data.put(name, request.getParameter(name));
            
            results = request.call(constructPath(lookup.get(verb).getService(), parameters), lookup.get(verb).getMethod(), data, response);
            path = path.substring(0, path.length() - verb.length() - 1);
            
            if(lookup.get(verb).getRedirect() != null) 
            {
                iterator = results.iterator();
                
                if(iterator.hasNext()) parameters.putAll(iterator.next());

                path = constructPath(lookup.get(verb).getRedirect(), parameters);
                
                if(!path.startsWith("/")) path = "/" + path;
                
                path = request.getContextPath() + path;
            }
            
            if(request.getQueryString() != null) path += "?" + request.getQueryString();
            
            response.sendRedirect(path);
            return;
        }
        
        if(lookup.containsKey(verb)) page = lookup.get(verb).getPage();
        if(page == null) throw new ConvirganceException("No handler for /" + verb + " and no default view page to render for " + path);
        
        request.forward(page, parameters, response);
    }
}
