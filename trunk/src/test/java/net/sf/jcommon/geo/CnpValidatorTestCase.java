package net.sf.jcommon.geo;

import static org.junit.Assert.*;
import net.sf.jcommon.geo.CnpValidator;

import org.junit.*;

public class CnpValidatorTestCase {
    @Test public void testValidateConsistency() {
    	CnpValidator v = new CnpValidator();
        assertTrue("CNP 1780311221218 should be valid", v.isValid("1780311221218", null));
        assertTrue("CNP 1781106370059 should be valid", v.isValid("1781106370059", null));
        assertFalse("CNP 1831007341687 should be invalid", v.isValid("1831007341687", null));
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
