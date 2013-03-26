package net.sf.jcommon.io;

import java.io.IOException;
import java.io.Reader;

@Deprecated
public class EncodedUnicodeReader extends Reader {

    private Reader reader;

    public EncodedUnicodeReader(Reader reader) {
        this.reader = reader;
    }

    public EncodedUnicodeReader(Object lock, Reader reader) {
        super(lock);
        this.reader = reader;
    }

    public void close() throws IOException {
        reader.close();
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        int initialOff = off;
        int readed = 0, initialReaded;
        do {
            off += readed;
            len -= readed;
            initialReaded = reader.read(cbuf, off, len);
            if (initialReaded == -1) {
                readed = (readed == 0 ? -1 : 0);
                break;
            }
            readed = initialReaded;
            for (int i = off; i < off + readed; i++) {
                switch (cbuf[i]) {
                    case '\\':
                        char ch;
                        if (i + 1 < off + readed) {
                            ch = cbuf[i + 1];
                        }
                        else {
                            int x = reader.read();
                            if (x < 0) {
                                throw new IOException("Unfinished \\uxxxx encoding.");
                            }
                            readed++;
                            ch = (char)x;
                        }
                        switch (ch) {
                            case 'u':
                                int value = 0;
                                for (int j = i + 2; j < i + 6; j++) {
                                    char ch2;
                                    if (j < off + readed) {
                                        ch2 = cbuf[j];
                                    }
                                    else {
                                        int x = reader.read();
                                        if (x < 0) {
                                            throw new IOException("Unfinished \\uxxxx encoding.");
                                        }
                                        readed++;
                                        ch2 = (char)x;
                                    }
                                    switch (ch2) {
                                        case '0':
                                        case '1':
                                        case '2':
                                        case '3':
                                        case '4':
                                        case '5':
                                        case '6':
                                        case '7':
                                        case '8':
                                        case '9':
                                            value = (value << 4) + ch2 - '0';
                                            break;
                                        case 'a':
                                        case 'b':
                                        case 'c':
                                        case 'd':
                                        case 'e':
                                        case 'f':
                                            value = (value << 4) + 10 + ch2 - 'a';
                                            break;
                                        case 'A':
                                        case 'B':
                                        case 'C':
                                        case 'D':
                                        case 'E':
                                        case 'F':
                                            value = (value << 4) + 10 + ch2 - 'A';
                                            break;
                                        default:
                                            throw new IOException("Malformed \\uxxxx encoding.");
                                    }
                                }
                                cbuf[i] = (char)value;
                                System.arraycopy(cbuf, i + 6, cbuf, i + 6 - 5, off + readed);
                                readed -= 5;
                                break;
                            case 't':
                                cbuf[i] = '\t';
                                System.arraycopy(cbuf, i + 2, cbuf, i + 2 - 1, off + readed);
                                readed -= 1;
                                break;
                            case 'r':
                                cbuf[i] = '\r';
                                System.arraycopy(cbuf, i + 2, cbuf, i + 2 - 1, off + readed);
                                readed -= 1;
                                break;
                            case 'n':
                                cbuf[i] = '\n';
                                System.arraycopy(cbuf, i + 2, cbuf, i + 2 - 1, off + readed);
                                readed -= 1;
                                break;
                            case 'f':
                                cbuf[i] = '\f';
                                System.arraycopy(cbuf, i + 2, cbuf, i + 2 - 1, off + readed);
                                readed -= 1;
                                break;
                            case '\\':
                                cbuf[i] = '\\';
                                System.arraycopy(cbuf, i + 2, cbuf, i + 2 - 1, off + readed);
                                readed -= 1;
                                break;
                            default:
                                throw new IOException("Unknown special character \\" + ch + ".");
                        }
                }
            }
        } while (initialReaded > readed);
        return off + readed - initialOff;
    }

}
