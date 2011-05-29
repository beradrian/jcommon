package net.sf.jcommon.util;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * @author Adrian BER
 */
public class CNPTestCase {
    @Test public void testValidateConsistency() {
        assertTrue("Validate CNP doesn't work for valid CNP", CNP.validateConsistency("1780311221218"));
        assertTrue("Validate CNP doesn't work for valid CNP", CNP.validateConsistency("1781106370059"));
        assertFalse("Validate CNP doesn't work for invalid CNP", CNP.validateConsistency("1831007341687"));
    }

    @Test public void testCreateValidCNPs() throws Exception {
//        String s;
//        s = CNP.createValidCNP(true,  MedicalSystem.SHORT_DATE_FORMAT.parse("16-02-1988"));
//        System.out.println("s AG = " + s);
//        s = CNP.createValidCNP(false, MedicalSystem.SHORT_DATE_FORMAT.parse("11-11-1991"));
//        System.out.println("s DT = " + s);
//        s = CNP.createValidCNP(true,  MedicalSystem.SHORT_DATE_FORMAT.parse("15-06-1981"));
//        System.out.println("s TC = " + s);
    }
}
