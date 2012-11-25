package net.sf.jcommon.io;

//import org.junit.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.IOException;

/**
 */
public class EncodedUnicodeReaderTestCase {

    private EncodedUnicodeReader reader;

//    @Before
    public void setUp() throws Exception {
        reader = new EncodedUnicodeReader(new StringReader("Testing \\u0103unicode\\tspecial ch\\u0103ar"));
    }

//    @Test
    public void testRead() throws IOException {
        char[] buff = new char[4];
        assertEquals(2, reader.read(buff, 1, 2));
        reader.read(buff);
        assertEquals("Normal character not read corectly", 'g', reader.read());
        reader.read(buff);
        assertEquals("Encoded character not read corectly", '\u0103', buff[1]);
        assertEquals("Normal character after encoded one not read corectly", 'u', buff[2]);
        reader.read(buff, 2, 2);
        reader.read(buff, 1, 3);
        assertEquals("Special character not read corectly", '\t', reader.read());
        char[] bigbuff = new char[1024];
        assertEquals("Readed count char returned incorrectly", 13, reader.read(bigbuff, 0, 1024));
        assertEquals("Normal character after special one not read corectly", 'p', bigbuff[1]);
    }
}
