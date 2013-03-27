package se.vgregion.mobile.hriv.utils;

/**
 * Since the conversion WGS <-> RT90 can be carried out in multiple ways the conversion implementations should implement
 * this simple contract.
 * 
 * @author Jonas Liljenfeldt, Know IT
 */
public interface CoordinateTransformerService {

    /**
     * Returns RT90 X and Y coordinates for specified Gon.
     * 
     * @param lat Latitude in WGS84 degrees, decimal format
     * @param lon Longitude in WGS84 degrees, decimal format.
     * @return int array with RT90 coordinates.
     */
    int[] getRT90(double lat, double lon);

    /**
     * Returns WGS84 latitude and longitude in degrees, decimal format.
     * 
     * @param x RT90 X coordinate.
     * @param y RT90 Y coordinate.
     * @return double array with WGS84 coordinates, degrees in decimal format.
     */
    double[] getWGS84(int x, int y);
}