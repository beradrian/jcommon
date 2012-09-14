package net.sf.jcommon.io;

import java.io.InputStream;
import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * An input stream decorator for another inpt stream. The reading process can be interrupted
 * by calling {@link #interrupt} or {@link #interrupt(java.io.IOException)} which
 * will throw an exception on the next read attempt and close the decorated input stream.
 * @author Adrian BER
 */
public class InterruptibleInputStream extends InputStream {

    /** The exception to be thrown. If null then the stream is not yet interrupted. */
    private IOException interrupted;

    /** The decorated input stream. */
    private InputStream in;

    public InterruptibleInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        if (interrupted != null)
            throw interrupted;
        return in.read();
    }

    public int available() throws IOException {
        if (interrupted != null)
            throw interrupted;
        return in.available();
    }

    public void close() throws IOException {
        if (interrupted != null)
            in.close();
    }

    public synchronized void reset() throws IOException {
        if (interrupted != null)
            throw interrupted;
        in.reset();
    }

    public boolean markSupported() {
        return in.markSupported();
    }

    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    public long skip(long n) throws IOException {
        if (interrupted != null)
            throw interrupted;
        return in.skip(n);
    }

    public int read(byte b[]) throws IOException {
        if (interrupted != null)
            throw interrupted;
        return in.read(b);
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (interrupted != null)
            throw interrupted;
        return in.read(b, off, len);
    }

    public boolean isInterrupted() {
        return interrupted != null;
    }

    public void interrupt() {
        interrupt(new InterruptedIOException());
    }

    public void interrupt(IOException exc) {
        // check if not already interrupted
        if (interrupted != null)
            throw new IllegalStateException("Input stream already interrupted.");
        interrupted = exc;
        // close the decorated stream
        try {
            in.close();
        } catch (IOException e) {}
    }
}
