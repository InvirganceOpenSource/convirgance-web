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
import com.invirgance.convirgance.transform.Transformer;
import com.invirgance.convirgance.web.consumer.Consumer;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.origin.Origin;
import com.invirgance.convirgance.web.parameter.Parameter;
import com.invirgance.convirgance.wiring.annotation.Wiring;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for receiving and persisting data from HTTP requests.
 * InsertService processes data submission operations (typically POST requests) by
 * extracting data from HTTP requests, applying transformations, and persisting
 * the results.
 * 
 * <pre>
 * The service follows:
 * - Extract parameters from the request
 * - Obtain raw data using an Origin component
 * - Parse the data with an Input processor
 * - Apply optional transformations
 * - Persist the data through a Consumer
 * - Return any generated keys or results
 * </pre>
 * 
 * Use this service when you need to create endpoints for data submission,
 * form processing, or implementing POST/PUT operations in REST APIs.
 * 
 * @author jbanes
 */
@Wiring
public class InsertService implements Service
{
    private List<Parameter> parameters;
    private Input input;
    private Origin origin;
    private List<Transformer> transformers;
    private Consumer consumer;
    
    
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
     * Gets the input parser for the request data.
     *
     * @return The input parser
     */
    public Input getInput()
    {
        return input;
    }
    
    /**
     * Sets the input parser for the request data.
     *
     * @param input The input parser
     */
    public void setInput(Input input)
    {
        this.input = input;
    }
    
    /**
     * Gets the origin of the data to be processed.
     *
     * @return The data origin
     */
    public Origin getOrigin()
    {
        return origin;
    }
    
    /**
     * Sets the origin of the data to be processed.
     *
     * @param origin The data origin
     */
    public void setOrigin(Origin origin)
    {
        this.origin = origin;
    }
    
    /**
     * Gets the list of transformers to apply to the data.
     *
     * @return The transformers list
     */
    public List<Transformer> getTransformers()
    {
        return transformers;
    }
    
    /**
     * Sets the list of transformers to apply to the data.
     *
     * @param transformers The transformers list
     */
    public void setTransformers(List<Transformer> transformers)
    {
        this.transformers = transformers;
    }
    
    /**
     * Gets the consumer that will persist the processed data.
     *
     * @return The consumer
     */
    public Consumer getConsumer()
    {
        return consumer;
    }
    
    /**
     * Sets the consumer that will persist the processed data.
     *
     * @param consumer The consumer
     */
    public void setConsumer(Consumer consumer)
    {
        this.consumer = consumer;
    }
    
    // TODO: Validate the data with errors to stop insert. Failure should be trapped and returned as JSON?
    // TODO: Need to support sequences and return the keys
    
    /**
     * Executes the insert service, processing and persisting data from the HTTP request.
     * 
     * @param request The HTTP request containing the data to process
     * @param response The HTTP response to write results to
     * @throws ConvirganceException If an error occurs during processing
     */    
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
