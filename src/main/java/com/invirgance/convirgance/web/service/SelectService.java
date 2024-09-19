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

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.output.Output;
import com.invirgance.convirgance.target.OutputStreamTarget;
import com.invirgance.convirgance.transform.Transformer;
import com.invirgance.convirgance.web.binding.Binding;
import com.invirgance.convirgance.web.parameter.Parameter;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbanes
 */
public class SelectService implements Service
{
    private List<Parameter> parameters;
    private Binding binding;
    private List<Transformer> transformers;
    private Output output;

    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }
    
    public Binding getBinding()
    {
        return binding;
    }

    public void setBinding(Binding binding)
    {
        this.binding = binding;
    }

    public List<Transformer> getTransformers()
    {
        return transformers;
    }

    public void setTransformers(List<Transformer> transformers)
    {
        this.transformers = transformers;
    }

    public Output getOutput()
    {
        return output;
    }

    public void setOutput(Output output)
    {
        this.output = output;
    }
    
    public void execute(HttpRequest request, HttpResponse response)
    {
        JSONObject params = new JSONObject();
        Iterable<JSONObject> iterable;
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        if(this.transformers == null) this.transformers = new ArrayList<>();
        
        for(Parameter parameter : this.parameters)
        {
            params.put(parameter.getName(), parameter.getValue(request));
        }
        
        iterable = binding.getBinding(params);
        
        for(Transformer transformer : transformers)
        {
            iterable = transformer.transform(iterable);
        }
        
        response.setContentType(output.getContentType());
        
        output.write(new OutputStreamTarget(response.getOutputStream()), iterable);
    }
}