package net.sf.jcommon.io;

import java.io.IOException;
import java.io.StringReader;

import org.junit.*;
import static org.junit.Assert.*;

/**
 */
public class ReplaceTokensReaderTestCase {

	@Test
    public void testReplace() {
        ReplaceTokensReader rtr = new ReplaceTokensReader(new StringReader("Hello @who@"));
        rtr.addToken("who", "world");
        int c;
        StringBuffer s = new StringBuffer();
        try {
            while ((c = rtr.read()) != -1) {
                s.append((char)c);
            }
        } catch (IOException e) {
        }
        assertEquals("Hello world", s.toString());
    }
}
