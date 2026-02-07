/*
 * The MIT License
 *
 * Copyright 2026 jbanes.
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
package com.invirgance.convirgance.web.output;

import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.output.Output;
import com.invirgance.convirgance.output.OutputCursor;
import com.invirgance.convirgance.target.Target;
import com.invirgance.convirgance.web.servlet.ServiceState;
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
public class DownloadOutputTest
{
    @Test
    public void testComputeFilename()
    {
        var output = new DownloadOutput();
        var parameters = new JSONObject();
        
        parameters.put("var1", "xyz");
        parameters.put("var2", 123);
        
        output.setFilename(null);
        assertEquals("", output.computeFilename());
        
        output.setFilename("test.txt");
        assertEquals(";filename=\"test.txt\"", output.computeFilename());
        
        ServiceState.set("parameters", parameters);
        
        output.setFilename("myfile-{var1}.txt");
        assertEquals(";filename=\"myfile-xyz.txt\"", output.computeFilename());
        
        output.setFilename("myfile-{var1}-{var2}.txt");
        assertEquals(";filename=\"myfile-xyz-123.txt\"", output.computeFilename());
    }
}
