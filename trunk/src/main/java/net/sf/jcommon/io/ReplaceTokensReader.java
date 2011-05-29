package net.sf.jcommon.io;

import java.io.IOException;
import java.io.Reader;
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
public final class ReplaceTokensReader extends Reader {

    /** Default "begin token" character. */
    private static final char DEFAULT_BEGIN_TOKEN = '@';

    /** Default "end token" character. */
    private static final char DEFAULT_END_TOKEN = '@';

    /** Data to be used before reading from stream again */
    private String queuedData = null;

    /** replacement test from a token */
    private String replaceData = null;

    /** Index into replacement data */
    private int replaceIndex = -1;

    /** Index into queue data */
    private int queueIndex = -1;

    /** Hashtable to hold the replacee-replacer pairs (String to String). */
    private Map<String, String> tokens = new HashMap<String, String>();

    /** Character marking the beginning of a token. */
    private char beginToken = DEFAULT_BEGIN_TOKEN;

    /** Character marking the end of a token. */
    private char endToken = DEFAULT_END_TOKEN;

    private Reader in;

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public ReplaceTokensReader(final Reader in) {
        this.in = in;
    }

    private int getNextChar() throws IOException {
        if (queueIndex != -1) {
            final int ch = queuedData.charAt(queueIndex++);
            if (queueIndex >= queuedData.length()) {
                queueIndex = -1;
            }
            return ch;
        }

        return in.read();
    }

    /**
     * Returns the next character in the filtered stream, replacing tokens
     * from the original stream.
     *
     * @return the next character in the resulting stream, or -1
     * if the end of the resulting stream has been reached
     *
     * @exception IOException if the underlying stream throws an IOException
     * during reading
     */
    public final int read() throws IOException {

        if (replaceIndex != -1) {
            final int ch = replaceData.charAt(replaceIndex++);
            if (replaceIndex >= replaceData.length()) {
                replaceIndex = -1;
            }
            return ch;
        }

        int ch = getNextChar();

        if (ch == beginToken) {
            final StringBuffer key = new StringBuffer("");
            do  {
                ch = getNextChar();
                if (ch != -1) {
                    key.append((char) ch);
                } else {
                    break;
                }
            } while (ch != endToken);

            if (ch == -1) {
                if (queuedData == null || queueIndex == -1) {
                    queuedData = key.toString();
                } else {
                    queuedData
                        = key.toString() + queuedData.substring(queueIndex);
                }
                queueIndex = 0;
                return beginToken;
            } else {
                key.setLength(key.length() - 1);

                final String replaceWith = tokens.get(key.toString());
                if (replaceWith != null) {
                    if (replaceWith.length() > 0) {
                        replaceData = replaceWith;
                        replaceIndex = 0;
                    }
                    return read();
                } else {
                    String newData = key.toString() + endToken;
                    if (queuedData == null || queueIndex == -1) {
                        queuedData = newData;
                    } else {
                        queuedData = newData + queuedData.substring(queueIndex);
                    }
                    queueIndex = 0;
                    return beginToken;
                }
            }
        }
        return ch;
    }

    public void close() throws IOException {
        in.close();
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        return in.read(cbuf, off, len);
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


}
