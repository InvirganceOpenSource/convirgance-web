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
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.parameter.Parameter;
import java.util.ArrayList;
import java.util.List;


/**
 * Service implementation for retrieving and returning data in response to 
 * HTTP requests. SelectService processes data retrieval operations (typically GET requests) by
 * extracting parameters from requests, retrieving data through a Binding,
 * applying transformations, and formatting the results for the response.
 * 
 * <pre>
 * The service follows a configurable pipeline:
 * - Extract parameters from the request
 * - Store parameters in thread-local storage for filter access
 * - Retrieve data using a configured Binding
 * - Apply optional transformations
 * - Format and return the results using an Output
 * </pre>
 * 
 * Use this service when you need to create endpoints for data retrieval,
 * search interfaces, or implementing GET operations in REST APIs.
 *
 * @author jbanes
 */
public class SelectService implements Service
{
    private List<Parameter> parameters;
    private Binding binding;
    private List<Transformer> transformers;
    private Output output;

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
     * Gets the binding that provides the data source.
     *
     * @return The binding
     */    
    public Binding getBinding()
    {
        return binding;
    }
    
    /**
     * Sets the binding that provides the data source.
     *
     * @param binding The binding
     */
    public void setBinding(Binding binding)
    {
        this.binding = binding;
    }
    
    /**
     * Gets the list of transformers to apply to the retrieved data.
     *
     * @return The transformers list
     */
    public List<Transformer> getTransformers()
    {
        return transformers;
    }
    
    /**
     * Sets the list of transformers to apply to the retrieved data.
     *
     * @param transformers The transformers list
     */
    public void setTransformers(List<Transformer> transformers)
    {
        this.transformers = transformers;
    }
    
    /**
     * Gets the output formatter for the response.
     *
     * @return The output formatter
     */
    public Output getOutput()
    {
        return output;
    }
    
    /**
     * Sets the output formatter for the response.
     *
     * @param output The output formatter
     */
    public void setOutput(Output output)
    {
        this.output = output;
    }
    
    /**
     * Processes an HTTP request to retrieve data without generating a response.
     * <p>
     * This method implements the core data retrieval pipeline and can be used
     * by other components that need access to the data without writing to a response.
     * </p>
     * <ol>
     *   <li>Extract parameters from the HTTP request</li>
     *   <li>Store parameters in thread-local storage for access by other components</li>
     *   <li>Use the configured binding to retrieve data based on the parameters</li>
     *   <li>Apply any configured transformers to the data</li>
     * </ol>
     *
     * @param request The HTTP request containing the parameters
     * @return An iterable of JSONObjects containing the retrieved and transformed data
     */
    public Iterable<JSONObject> process(HttpRequest request)
    {
        JSONObject params = new JSONObject();
        Iterable<JSONObject> iterable;
        
        if(this.parameters == null) this.parameters = new ArrayList<>();
        if(this.transformers == null) this.transformers = new ArrayList<>();
        
        // Obtain the parameters for binding
        for(Parameter parameter : this.parameters)
        {
            params.put(parameter.getName(), parameter.getValue(request));
        }
        
        // Record the bindings to a thread local so it can be referenced deep in the heirarchy
        BindingParameter.bindings.set(params);
        
        // Generate the source of information by binding the parameters
        iterable = binding.getBinding(params);
        
        // Perform tranformations on the data
        for(Transformer transformer : transformers)
        {
            iterable = transformer.transform(iterable);
        }
        
        return iterable;
    }
    
    /**
     * Executes the select service, retrieving and returning data in 
     * response to an HTTP request.
     *
     * @param request The HTTP request containing the parameters
     * @param response The HTTP response to write results to
     */
    @Override
    public void execute(HttpRequest request, HttpResponse response)
    {
        Iterable<JSONObject> iterable = process(request);
        
        // Write out the response
        response.setContentType(output.getContentType());
        
        output.write(new OutputStreamTarget(response.getOutputStream()), iterable);
        
        // Clean up after ourselves
        BindingParameter.bindings.remove();
    }
}
