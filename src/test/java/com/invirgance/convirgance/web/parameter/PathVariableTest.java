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
package com.invirgance.convirgance.web.parameter;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.web.http.HttpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jbanes
 */
public class PathVariableTest
{
    
    public PathVariableTest()
    {
    }
    
    @BeforeAll
    public static void setUpClass()
    {
    }
    
    @AfterAll
    public static void tearDownClass()
    {
    }
    
    @BeforeEach
    public void setUp()
    {
    }
    
    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testGetName()
    {
        var parameter = new PathVariable();
        
        parameter.setPath("/bubba/{bob}");
        assertEquals("bob", parameter.getName());
        
        parameter.setPath("/bubba/{bob}/whatever");
        assertEquals("bob", parameter.getName());
        
        parameter.setPath("/*/{bob}/*/");
        assertEquals("bob", parameter.getName());
        
        try
        {
            parameter.setPath("/*/{bob}/posts/{whatever}/");
            assertEquals("bob", parameter.getName());
            fail("Expected bob to fail");
        }
        catch(ConvirganceException e)
        {
            assertEquals("Path /*/{bob}/posts/{whatever}/ has more than one defined variable. Use a * for unknown parts of the path.", e.getMessage());
        }
        
        try
        {
            parameter.setPath("/*/bob/posts/whatever/");
            assertEquals("bob", parameter.getName());
            fail("Expected bob to fail");
        }
        catch(ConvirganceException e)
        {
            assertEquals("Path /*/bob/posts/whatever/ does not contain any variables", e.getMessage());
        }
        
        try
        {
            parameter.setPath("/*/{bob/posts/whatever/");
            assertEquals("bob", parameter.getName());
            fail("Expected bob to fail");
        }
        catch(ConvirganceException e)
        {
            assertEquals("Path /*/{bob/posts/whatever/ does not have a terminating '}'", e.getMessage());
        }
    }
    
    
    @Test
    public void testGetValue()
    {
        var parameter = new PathVariable();
        
        parameter.setPath("/bubba/{bob}");
        assertEquals("123", parameter.getValue(MockRequest.request("/bubba/123")));
        
        parameter.setPath("/bubba/{bob}/posts/*");
        assertEquals("123", parameter.getValue(MockRequest.request("/bubba/123/posts/567")));
        
        parameter.setPath("/bubba/{bob}");
        assertNull(parameter.getValue(MockRequest.request("/jukebox/123/posts/567")));
        
        parameter.setPath("/bubba/xy{bob}ab");
        assertEquals("123", parameter.getValue(MockRequest.request("/bubba/xy123ab/posts/567")));
        
        parameter.setPath("/bubba/xy{bob}ab");
        assertNull(parameter.getValue(MockRequest.request("/bubba/123/posts/567")));
        
        parameter.setPath("/bubba/{bob}");
        assertEquals("123", parameter.getValue(MockRequest.request("/test/bubba/123", "/test")));
    }
    
    public static class MockRequest
    {
        private String context;
        private String path;
        
        public MockRequest(String path, String context)
        {
            this.path = path;
            this.context = context;
        }
        
        public String getRequestURI()
        {
            return path;
        }
        
        public String getContextPath()
        {
            return context;
        }
        
        public static HttpRequest request(String path)
        {
            return new HttpRequest(new MockRequest(path, null));
        }
        
        public static HttpRequest request(String path, String context)
        {
            return new HttpRequest(new MockRequest(path, context));
        }
    }
}
