package net.sf.jcommon.io;

import java.io.*;

/**
 * Redirects an output stream to a writer.
 * @author Adrian Ber &lt;beradrian@yahoo.com&gt;
 * @version 1.0
 */
public class WriterOutputStream extends OutputStream {

    protected Writer writer;

    public WriterOutputStream(Writer writer) {
        this.writer = writer;
    }

    public void write(int b) throws IOException {
        writer.write(b);
    }
}