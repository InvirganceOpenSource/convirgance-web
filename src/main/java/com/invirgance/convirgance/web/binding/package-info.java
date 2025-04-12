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

/**
 * Data retrieval and binding API.
 * 
 * <p>This package provides interfaces and implementations for sourcing 
 * JSON data from various origins. Components in this package handle the "read" 
 * side of data processing workflows, retrieving data that can then be 
 * transformed and consumed based on request parameters.</p>
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link Binding} - Core interface for components that retrieve JSON data
 *      from various sources</li>
 *   <li>{@link QueryBinding} - Database binding that executes SQL queries with
 *      parameter binding</li>
 *   <li>{@link FileSystemInputBinding} - Binding for reading JSON from files on
 *      the filesystem</li>
 *   <li>{@link ClasspathInputBinding} - Binding for reading JSON from the 
 *      application classpath</li>
 * </ul>
 * 
 * <p>Typical usage scenarios:</p>
 * <ul>
 *   <li>Retrieving data from databases for web service responses</li>
 *   <li>Loading configuration or reference data from files</li>
 *   <li>Binding HTTP request parameters to data queries</li>
 *   <li>Creating data retrieval endpoints with minimal custom code</li>
 * </ul>
 * 
 * <p>These components are typically configured in Spring XML.</p>
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.binding;
