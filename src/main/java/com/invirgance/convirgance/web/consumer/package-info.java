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

/**
 * This package provides interfaces and implementations for persisting JSON 
 * data to various storage backends. Components in this package handle the 
 * "write" side of data processing workflows, typically receiving processed data
 * and saving it to a destination.
 * 
 * <p>Key components:</p>
 * <ul>
 *   <li>{@link Consumer} - Core interface for components that process JSON data
 *       and persist it to some destination</li>
 *   <li>{@link QueryConsumer} - SQL database implementation that executes 
 *       parameterized queries with optional sequence ID generation</li>
 * </ul>
 * 
 * <p>Typical usage scenarios:</p>
 * <ul>
 *   <li>Persisting web service data to databases</li>
 *   <li>Writing processed JSON to external systems</li>
 *   <li>Recording data with auto-generated IDs</li>
 * </ul>
 * 
 * <p>These components are typically configured in Spring XML and used within
 * {@link InsertService} implementations.</p>
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.consumer;
