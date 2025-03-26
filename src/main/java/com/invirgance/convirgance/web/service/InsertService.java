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
import com.invirgance.convirgance.input.Input;
import com.invirgance.convirgance.json.JSONArray;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.json.JSONWriter;
import com.invirgance.convirgance.transform.Transformer;
import com.invirgance.convirgance.web.consumer.Consumer;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.origin.Origin;
import com.invirgance.convirgance.web.parameter.Parameter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbanes
 */
public class InsertService implements Service
{
    private List<Parameter> parameters;
    private Input input;
    private Origin origin;
    private List<Transformer> transformers;
    private Consumer consumer;

    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public Input getInput()
    {
        return input;
    }

    public void setInput(Input input)
    {
        this.input = input;
    }

    public Origin getOrigin()
    {
        return origin;
    }

    public void setOrigin(Origin origin)
    {
        this.origin = origin;
    }

    public List<Transformer> getTransformers()
    {
        return transformers;
    }

    public void setTransformers(List<Transformer> transformers)
    {
        this.transformers = transformers;
    }

    public Consumer getConsumer()
    {
        return consumer;
    }

    public void setConsumer(Consumer consumer)
    {
        this.consumer = consumer;
    }
    
    // TODO: Validate the data with errors to stop insert. Failure should be trapped and returned as JSON?
    // TODO: Need to support sequences and return the keys
    
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        JSONObject params = new JSONObject();
        Iterable<JSONObject> iterable;
        
        JSONObject result = new JSONObject("{\"success\":true}");
        JSONArray keys;
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        if(this.transformers == null) this.transformers = new ArrayList<>();
        
        // Obtain the parameters for binding
        for(Parameter parameter : this.parameters)
        {
            params.put(parameter.getName(), parameter.getValue(request));
        }
        
        // Get Source and Input to parse Iterable stream
        iterable = input.read(origin.getOrigin(request, params));

        // Perform tranformations on the data
        for(Transformer transformer : transformers)
        {
            iterable = transformer.transform(iterable);
        }
        
        // Consume the uploaded stream of data
        keys = consumer.consume(iterable, params);
        
        // Write out keys if they exist
        try 
        {
            if(keys != null)
            {
                response.setContentType("application/json");
                response.getOutputStream().write(keys.toString().getBytes("UTF-8"));
            }
        }
        catch(IOException e) { throw new ConvirganceException(e); }
    }
}
