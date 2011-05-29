package net.sf.jcommon.io;

import java.io.*;

/**
 * Redirects an input stream to a reader.
 * @author Adrian Ber &lt;beradrian@yahoo.com&gt;
 * @version 1.0
 */
public class ReaderInputStream extends InputStream {

    protected Reader reader;

    public ReaderInputStream(Reader reader) {
        this.reader = reader;
    }

    public int read() throws IOException {
        return reader.read();
    }
}