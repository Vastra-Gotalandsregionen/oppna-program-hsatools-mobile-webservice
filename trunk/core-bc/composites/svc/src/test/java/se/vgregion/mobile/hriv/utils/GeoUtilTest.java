package se.vgregion.mobile.hriv.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Patrik Bergström
 */
public class GeoUtilTest {

    @Test
    public void testParseRT90HsaString() throws Exception {
        int[] ints1 = GeoUtil.parseRT90HsaString("X:6477382,Y:1272256");
        int[] ints2 = GeoUtil.parseRT90HsaString("X: 6477382, Y: 1272256");

        assertEquals(6477382, ints1[0]);
        assertEquals(1272256, ints1[1]);
        assertEquals(6477382, ints2[0]);
        assertEquals(1272256, ints2[1]);
    }
}
