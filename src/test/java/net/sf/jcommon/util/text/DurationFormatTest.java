package net.sf.jcommon.util.text;

import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatTest {

	@Test
	public void testDuration() {
		assertEquals("0:02:01.000", DurationFormat.format(121000, "HHH:mm:ss.SSS"));
	}
	
}
