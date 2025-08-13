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
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.parameter.Parameter;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jbanes
 */
@Wiring
public class HypermediaService implements Service
{
    // Parameter extraction that will be turned into request parameters
    // Service to handle POST/PUT/DELETE, should automatically transform form submission into record
    // Optional header/footer composition
    private List<Parameter> parameters;
    private String page;
    private Map<String,String> verbs;

    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    public Map<String, String> getVerbs()
    {
        return verbs;
    }

    public void setVerbs(Map<String, String> verbs)
    {
        this.verbs = verbs;
    }
    
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        var parameters = new JSONObject();
        var path = request.getRequestURI();
        var verb = path.substring(path.lastIndexOf('/')+1);
        var page = this.page;
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        
        // Obtain the parameters for binding
        for(Parameter parameter : this.parameters)
        {
            parameters.put(parameter.getName(), parameter.getValue(request));
        }
        
        if(verbs.containsKey(verb)) page = verbs.get(verb);
        
        request.forward(page, parameters, response);
    }
}
