package net.sf.jcommon.io;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

/**
 */
public class ReplaceTokensWriterTestCase {

	@Test
    public void testReplace() throws IOException {
        StringWriter sw = new StringWriter();
        ReplaceTokensWriter rtw = new ReplaceTokensWriter(sw);
        rtw.addToken("who", "world");
        rtw.write("Hello @who@");
        assertEquals("Hello world", sw.getBuffer().toString());
    }
}
