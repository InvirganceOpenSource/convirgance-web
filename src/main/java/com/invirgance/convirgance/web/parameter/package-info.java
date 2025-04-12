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
 * Parameter extraction and binding API.
 * This package provides interfaces and implementations for extracting values 
 * from HTTP requests and making them available to services. Parameters act as 
 * the bridge between HTTP requests and the framework's data processing components.
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link Parameter} - Core interface for components that extract values 
 *       from HTTP requests</li>
 *   <li>{@link RequestParameter} - Extracts single values from request 
 *        parameters with optional default values</li>
 *   <li>{@link RequestArrayParameter} - Extracts multiple values as arrays from
 *        request parameters</li>
 *   <li>{@link StaticParameter} - Provides fixed values that don't depend on 
 *       the request</li>
 * </ul>
 *
 * <pre>
 * Typical usage scenarios:
 * - Extracting query parameters for database operations
 * - Mapping request values to binding parameters
 * - Providing default values for optional parameters
 * - Combining request values with fixed configuration values
 * </pre>
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.parameter;
