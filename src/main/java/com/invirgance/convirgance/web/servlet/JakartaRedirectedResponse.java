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
package com.invirgance.convirgance.web.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author jbanes
 */
public class JakartaRedirectedResponse extends HttpServletResponseWrapper
{
    private ByteArrayOutputStream output;
    private HttpServletResponse response;
    
    public JakartaRedirectedResponse(Object response)
    {
        this((HttpServletResponse)response, false);
    }
    
    public JakartaRedirectedResponse(Object response, boolean capture)
    {
        super((HttpServletResponse)response);
        
        if(capture) output = new ByteArrayOutputStream();
        
        this.response = (HttpServletResponse)response;
    }
    
    public byte[] getOutput()
    {
        if(output == null) return null;
        
        return output.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        return new ServletOutputStreamWrapper();
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        return new PrintWriter(getOutputStream());
    }
    
    private class ServletOutputStreamWrapper extends ServletOutputStream
    {
        @Override
        public boolean isReady()
        {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener wl)
        {
            throw new UnsupportedOperationException("Write listeners are not currently supported when calling paths from a service");
        }

        @Override
        public void write(int b) throws IOException
        {
            if(output != null) output.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException
        {
            if(output != null) output.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            if(output != null) output.write(b, off, len);
        }
    }
}
