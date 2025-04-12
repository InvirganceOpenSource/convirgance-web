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
package com.invirgance.convirgance.web.binding;

import com.invirgance.convirgance.input.Input;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.source.ClasspathSource;

/**
 * Retrieves files from within the applications class path.
 * Allows services to access JSON files packaged within the application's JAR or
 * WAR files or located in the classpath directories.
 * 
 * @author jbanes
 */
public class ClasspathInputBinding implements Binding
{
    private Input<JSONObject> input;
    private String path;
    
    /**
     * Returns the configured input processor.
     * 
     * @return The {@link Input} of the {@link JSONObject}.
     */
    public Input<JSONObject> getInput()
    {
        return input;
    }

    /**
     * Updates the type of {@link Input} expected for the JSONObject.
     * 
     * @param input The {@link Input} for a {@link JSONObject}.
     */
    public void setInput(Input<JSONObject> input)
    {
        this.input = input;
    }

    /**
     * Returns the current path of the JSON file.
     * 
     * @return The path.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path to load the JSON file from.
     * 
     * @param path The path.
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * Retrieves and parses the resource from the class path.
     * 
     * @param parameters Required by the Binding interface but not used in this implementation
     * @return An {@link Iterable} of {@link JSONObject}s from the classpath resource
     */
    @Override
    public Iterable<JSONObject> getBinding(JSONObject parameters)
    {
        return input.read(new ClasspathSource(path));
    }
    
}
