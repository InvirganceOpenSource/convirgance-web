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
 * Core web service implementations.
 * Services handle HTTP requests and responses,
 * orchestrating data retrieval, transformation, and persistence operations.
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link Service} - Core interface defining the contract for all service
 *       implementations</li>
 *   <li>{@link SelectService} - Implementation for retrieving and returning
 *       data (e.g., GET operations)</li>
 *   <li>{@link InsertService} - Implementation for accepting and persisting 
 *       data (e.g., POST operations)</li>
 *   <li>{@link BindingParameter} - Utility for sharing parameter values during
 *       request processing</li>
 *   <li>{@link BindingFilter} - Filter implementation that uses request 
 *       parameters in filtering operations</li>
 * </ul>
 * 
 * <pre>
 * Typical usage scenarios:
 * - Creating RESTful data access endpoints
 * - Building data submission endpoints
 * - Implementing CRUD operations for web applications
 * - Configuring declarative web services with minimal code
 * </pre>
 * 
 * <p>Services are typically configured in Spring XML files and loaded by the framework's
 * servlet components. Each service coordinates multiple components including parameters,
 * bindings, transformers, and consumers to process HTTP requests and generate responses.</p>
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.service;

