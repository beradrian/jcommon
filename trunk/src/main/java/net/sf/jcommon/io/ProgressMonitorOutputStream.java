package net.sf.jcommon.io;

import javax.swing.*;
import java.io.*;
import java.awt.Component;


public class ProgressMonitorOutputStream extends FilterOutputStream {
    private ProgressMonitor monitor;
    private int nwritten = 0;


    public ProgressMonitorOutputStream(Component parentComponent,
            Object message, OutputStream out, int totalSize) {
        super(out);
        monitor = new ProgressMonitor(parentComponent, message, null, 0, totalSize);
    }


    public ProgressMonitor getProgressMonitor() {
        return monitor;
    }

    public void close() throws IOException {
        out.close();
        monitor.close();
    }

    public void flush() throws IOException {
        if (monitor.isCanceled()) {
            close();
            return;
        }
        out.flush();
    }

    public void write(int b) throws IOException {
        if (monitor.isCanceled()) {
            close();
            return;
        }
        out.write(b);
        nwritten++;
        monitor.setProgress(nwritten);
    }

    public void write(byte b[]) throws IOException {
        if (monitor.isCanceled()) {
            close();
            return;
        }
        out.write(b);
        nwritten += b.length;
        monitor.setProgress(nwritten);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (monitor.isCanceled()) {
            close();
            return;
        }
        out.write(b, off, len);
        nwritten += len;
        monitor.setProgress(nwritten);
    }

}
