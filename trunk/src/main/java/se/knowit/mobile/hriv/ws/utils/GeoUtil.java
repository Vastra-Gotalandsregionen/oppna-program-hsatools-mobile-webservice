package se.knowit.mobile.hriv.ws.utils;


/**
 * Utility-class for geographically related conversions etc.
 */
public abstract class GeoUtil {

	  /**
	   * @param rt90String HSA formatted RT90 coords: X: 1234567, Y: 1234567.
	   * @return An array of RT90 coordinates.
	   */
	  public static int[] parseRT90HsaString(String rt90String) {
	    int[] result = null;
	    if (!"".equals(rt90String)) {
	      if (rt90String.indexOf("X:") >= 0 && rt90String.indexOf("Y:") >= 0) {
	        int rt90X = Integer.parseInt(rt90String.substring(3, 10));
	        int rt90Y = Integer.parseInt(rt90String.substring(15));
	        result = new int[] { rt90X, rt90Y };
	      }
	    }
	    return result;
	  }
}
