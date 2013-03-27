package se.vgregion.mobile.hriv.utils;


import java.util.Scanner;

/**
 * Utility-class for geographically related conversions etc.
 */
public abstract class GeoUtil {

    /**
     * Parses an RT90 formatted string.
     *
     * @param rt90String HSA formatted RT90 coords: X: 1234567, Y: 1234567.
     * @return An array of RT90 coordinates.
     */
    public static int[] parseRT90HsaString(String rt90String) {
        int[] result = null;
        if (!"".equals(rt90String)) {
            if (rt90String.indexOf("X:") >= 0 && rt90String.indexOf("Y:") >= 0) {
                // Use Scanner to find next group of consecutive numbers.
                // See se.vgregion.mobile.hriv.utils.GeoUtilTest.testParseRT90HsaString().
                Scanner scanner = new Scanner(rt90String); // E.g. "X:6477382,Y:1272256"
                int rt90X = Integer.parseInt(scanner.findInLine("\\d+")); // Will then return 6477382
                int rt90Y = Integer.parseInt(scanner.findInLine("\\d+")); // Will then return 1272256
                result = new int[]{rt90X, rt90Y};
            }
        }
        return result;
    }
}
