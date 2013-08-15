package net.sf.jcommon.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class RedirectedHttpServletResponse extends HttpServletResponseWrapper {

    private static class RedirectedServletOutputStream extends ServletOutputStream {

        private OutputStream out;

        public RedirectedServletOutputStream(OutputStream out) {
            this.out = out;
        }

        public void write(int b) throws IOException {
            out.write(b);
        }

        public void write(byte b[], int off, int len) throws IOException {
            out.write(b, off, len);
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

    private ServletOutputStream sout;
    private PrintWriter writer;

    public RedirectedHttpServletResponse(HttpServletResponse httpServletResponse, OutputStream out) {
        super(httpServletResponse);
        sout = new RedirectedServletOutputStream(out);
        writer = new PrintWriter(new OutputStreamWriter(sout));
    }

    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return sout;
    }

    public void flushBuffer() throws IOException {
        writer.flush();
        sout.flush();
    }

}
