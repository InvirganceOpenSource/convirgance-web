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
 * Provides custom JSP tag implementations for building dynamic web applications.
 * <pre>
 * Contains a collection of JSP tags that facilitate 
 * common tasks in web development:
 * - Data manipulation (JSON objects, arrays, key-value pairs)
 * - Database access and querying
 * - Service integration
 * - Collection iteration and processing
 * - Variable management across different scopes
 * </pre>
 * 
 * The tags in this package are designed to work together to create a cohesive and
 * flexible system for developing JSP-based web applications. Container tags like
 * {@link ObjectTag} and {@link ArrayTag} can be populated using nested child 
 * tags like {@link KeyTag} and {@link ValueTag}.
 * 
 * Common interfaces like {@link ValueTypeTag} and {@link KeyValueTypeTag} 
 * establish the communication patterns between parent and child tags in the hierarchy.
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.tag;
