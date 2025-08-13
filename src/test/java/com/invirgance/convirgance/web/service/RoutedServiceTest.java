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
package com.invirgance.convirgance.web.service;

import com.invirgance.convirgance.ConvirganceException;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.web.http.HttpRequest;
import com.invirgance.convirgance.web.http.HttpResponse;
import java.util.Map;
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
public class RoutedServiceTest
{

    @Test
    public void testExecute()
    {
        RoutedService routed = new RoutedService();
        Map routes = new JSONObject();
        
        MockService owner1 = new MockService();
        MockService owner2 = new MockService();
        MockService pet = new MockService();
        
        routes.put("/owner", owner1);
        routes.put("/owner/*", owner2);
        routes.put("/owner/*/pet/*", pet);
        
        routed.setRoutes((Map<String,Service>)routes);
        
        routed.execute(new HttpRequest(new RouteRequest("/owner")), null);
        routed.execute(new HttpRequest(new RouteRequest("/owner/123")), null);
        routed.execute(new HttpRequest(new RouteRequest("/owner/123/pet/456")), null);
        
        assertTrue(owner1.isCalled());
        assertTrue(owner2.isCalled());
        assertTrue(pet.isCalled());
    }
    
    public static class RouteRequest
    {
        private String path;
        private String context;

        public RouteRequest(String path)
        {
            this(path, "");
        }
        
        public RouteRequest(String path, String context)
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
    }
    
    public static class MockService implements Service
    {
        private boolean called;
        
        @Override
        public void execute(HttpRequest request, HttpResponse response)
        {
            if(called) throw new ConvirganceException("Route already called");
            
            called = true;
        }

        public boolean isCalled()
        {
            return called;
        }
    }
}
