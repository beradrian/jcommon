package net.sf.jcommon.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;

/**
 * Replaces tokens in the original input with user-supplied values.
 * A token is a key delimited by the begin token character (default @)
 * and end token character (default @). The string between these characters is the name
 * of the token and this entire construct will be replaced by the token value.
 * This class is heavily based on the class <code>org.apache.ant.filters.ReplaceTokens</code>
 * @author Adrian BER (adrian.ber@greefsoftware.com)
 */
public final class ReplaceTokensWriter extends Writer {

    /** Default "begin token" character. */
    private static final char DEFAULT_BEGIN_TOKEN = '@';

    /** Default "end token" character. */
    private static final char DEFAULT_END_TOKEN = '@';

    /** Hashtable to hold the replacee-replacer pairs (String to String). */
    private Map<String, String> tokens = new HashMap<String, String>();

    /** Character marking the beginning of a token. */
    private char beginToken = DEFAULT_BEGIN_TOKEN;

    /** Character marking the end of a token. */
    private char endToken = DEFAULT_END_TOKEN;

    private Writer out;

    /**
     * Creates a new filtered writer.
     *
     * @param out A Writer object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public ReplaceTokensWriter(final Writer out) {
        this.out = out;
    }

    StringBuffer buff = new StringBuffer();
    boolean inToken = false;

    public void write(int c) throws IOException {
        if (!inToken) {
            if (c == beginToken) {
                inToken = true;
            } else {
                out.write((char)c);
            }
        } else {
            if (c == endToken) {
                String s = buff.toString();
                Object value = tokens.get(s);
                if (value != null) {
                    s = value.toString();
                } else {
                    s = beginToken + s + endToken;
                }
                out.write(s);
                buff.setLength(0);
                inToken = false;
            } else {
                buff.append((char)c);
            }
        }
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        for (int i = off; i < cbuf.length && i < off + len; i++) {
            write(cbuf[i]);
        }
    }

    public void write(char cbuf[]) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    public void write(String str) throws IOException {
        write(str.toCharArray());
    }

    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }

    public void close() throws IOException {
        out.close();
    }

    /**
     * Sets the "begin token" character.
     *
     * @param beginToken the character used to denote the beginning of a token
     */
    public void setBeginToken(final char beginToken) {
        this.beginToken = beginToken;
    }

    /**
     * Returns the "begin token" character.
     *
     * @return the character used to denote the beginning of a token
     */
    public char getBeginToken() {
        return beginToken;
    }

    /**
     * Sets the "end token" character.
     *
     * @param endToken the character used to denote the end of a token
     */
    public void setEndToken(final char endToken) {
        this.endToken = endToken;
    }

    /**
     * Returns the "end token" character.
     *
     * @return the character used to denote the end of a token
     */
    public char getEndToken() {
        return endToken;
    }

    /**
     * Adds a token element to the map of tokens to replace.
     *
     * @param key token name
     * @param value token value
     */
    public void addToken(final String key, final String value) {
        tokens.put(key, value);
    }

    public void addTokens(Map<String, String> tokens) {
        this.tokens.putAll(tokens);
    }
}
