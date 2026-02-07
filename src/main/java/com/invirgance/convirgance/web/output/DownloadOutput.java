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
import com.invirgance.convirgance.web.http.HttpResponse;
import com.invirgance.convirgance.web.servlet.ServiceState;
import com.invirgance.convirgance.wiring.annotation.Wiring;

/**
 * A wrapper to force the user's browser to download the output instead of 
 * opening it. This is typically used to force a download of spreadsheet formats 
 * like CSV, though it can be used to wrap browser formats like JSON. 
 * 
 * A filename can be optionally specified to set the name of the file to 
 * download. Curly braces can be used to inject parameters into the filename. For
 * example, setting filename to <code>"Report {generate_date}.csv"</code> would 
 * replace <code>{generate_date}</code> with the value from the parameters list.
 * 
 * @author jbanes
 */
@Wiring
public class DownloadOutput implements Output
{
    private Output output;
    private String filename;

    /**
     * Returns the {@link Output} object being wrapped.
     * 
     * @return the wrapped output
     */
    public Output getOutput()
    {
        return output;
    }

    /**
     * Set the {@link Output} object to wrap.  
     * 
     * @param output generates the data downloaded by the client
     */
    public void setOutput(Output output)
    {
        this.output = output;
    }

    /**
     * The name of the downloaded file. 
     * 
     * @return the name of the downloaded file or null if no filename was set
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Set the name of the downloaded file. Values from the parameters list can be
     * injected using curly braces. For example, setting filename to <code>"Report 
     * {generate_date}.csv"</code> would replace <code>{generate_date}</code> with 
     * the value from the parameters list.
     * 
     * @param filename the name of the downloaded file
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    
    String computeFilename()
    {
        var parameters = (JSONObject)ServiceState.get("parameters");
        var buffer = new StringBuffer(";filename=\"");
        var token = new StringBuffer();
        
        char c;
        
        if(this.filename == null) return "";
        
        for(var i=0; i<this.filename.length(); i++)
        {
            c = this.filename.charAt(i);
            
            if(c == '{')
            {
                while((c = this.filename.charAt(++i)) != '}') token.append(c);
                
                buffer.append(parameters.getString(token.toString(), ""));
                token.setLength(0);
                
                continue;
            }
            
            buffer.append(c);
        }
        
        buffer.append('"');
        
        return buffer.toString();
    }
    
    @Override
    public OutputCursor write(Target target)
    {
        var response = (HttpResponse)ServiceState.get("response");

        response.setHeader("Content-Disposition", "attachment" + computeFilename());
        
        return output.write(target);
    }

    /**
     * Returns the MIME type of the wrapped {@link Output} object.
     * 
     * @return the MIME type of the output
     */
    @Override
    public String getContentType()
    {
        return output.getContentType();
    }
}
