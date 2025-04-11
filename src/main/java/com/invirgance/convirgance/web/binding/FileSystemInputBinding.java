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
import com.invirgance.convirgance.source.FileSource;
import java.io.File;

/**
 * Retrieves files from the file system.
 * FileSystemInputBinding allows services to access JSON files stored on the 
 * server's file system. This is useful for data that may change without 
 * redeploying the application, such as configuration files,
 * external data feeds, or shared resources.
 * 
 * @author jbanes
 */
public class FileSystemInputBinding implements Binding
{
    private Input<JSONObject> input;
    private String path;

    /**
     * Returns the current {@link Input} of the {@link JSONObject}
     * 
     * @return The {@link Input} to the JSONObject.
     */
    public Input<JSONObject> getInput()
    {
        return input;
    }

    /**
     * Sets the {@link JSONObject}'s {@link Input}
     * 
     * @param input The input type.
     */
    public void setInput(Input<JSONObject> input)
    {
        this.input = input;
    }

    /**
     * Gets the current path of the bound JSONObject.
     * 
     * @return The path.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the file system path.
     * Spring expressions can be used.
     * 
     * Example: value="#{systemProperties['database.root']}/data/customers.json"
     * 
     * @param path The location.
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public Iterable<JSONObject> getBinding(JSONObject parameters)
    {
        return input.read(new FileSource(new File(path)));
    }
    
}
