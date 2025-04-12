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
 * Provides servlet components and service management utilities for web 
 * applications.
 * 
 * 
 * This package contains:
 * <ul>
 *   <li>Servlet implementations that serve as entry points for HTTP requests in both
 *       Jakarta EE ({@link com.invirgance.convirgance.web.servlet.JakartaServicesServlet})
 *       and Java EE ({@link com.invirgance.convirgance.web.servlet.JavaEEServicesServlet})
 *       environments</li>
 *   <li>Utility classes for invoking services ({@link ServiceCaller}) 
 * programmatically from within web applications</li>
 *   <li>Application initialization components ({@link ApplicationInitializer}) 
 * for setting up database connections and performing startup tasks</li>
 * </ul>
 * 
 * 
 * @author jbanes
 */
package com.invirgance.convirgance.web.servlet;
