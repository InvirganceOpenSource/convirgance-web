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
 * Data source interfaces for processing HTTP request data.
 * This package provides components that act as data sources for insertion operations,
 * extracting raw data from HTTP requests.
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link Origin} - Core interface for components that extract source data
 *       from HTTP requests</li>
 *   <li>{@link RequestBodyOrigin} - Provides access to the HTTP request body as
 *       a data source</li>
 *   <li>{@link ParameterOrigin} - Extracts data from a specific HTTP request 
 *       parameter</li>
 * </ul>
 * 
 * <pre>
 * Typical usage scenarios:
 * - Processing JSON or XML request bodies for persistence
 * - Handling form submissions or file uploads
 * - Extracting specific parameters for insertion operations
 * - Creating data insertion endpoints with flexible data sources
 * </pre>
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.origin;
