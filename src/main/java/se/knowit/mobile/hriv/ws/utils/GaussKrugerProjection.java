package se.knowit.mobile.hriv.ws.utils;

/**
 * Uses "Gauss konforma projektion" for conversion WGS84 <-> RT90.
 * 
 * @see http://sv.wikipedia.org/wiki/Gauss_projektion
 * @see http://www.lantmateriet.se/templates/LMV_Page.aspx?id=4766
 * @see http://www.lantmateriet.se/upload/filer/kartor/geodesi_gps_och_detaljmatning/geodesi/Formelsamling/
 *      Gauss_Conformal_Projection.pdf
 * @see http 
 *      ://www.lantmateriet.se/upload/filer/kartor/geodesi_gps_och_detaljmatning/Transformationer/SWEREF99_RT90_Samband
 *      /Transformationsparametrar_pdf.pdf
 * @see http://www.lantmateriet.se/templates/LMV_Page.aspx?id=3003
 * @see http://www.lantmateriet.se/upload/filer/kartor/geodesi_gps_och_detaljmatning/Kartprojektioner/Oversikt/
 *      kartprojektioner_oversikt.pdf
 * @see http://www.lantmateriet.se/upload/filer/kartor/geodesi_gps_och_detaljmatning/Kartprojektioner/Oversikt/Gauss-
 *      kruger_projektion.pdf
 * @see http://www.lantmateriet.se/templates/LMV_Page.aspx?id=5197
 * @see http://sv.wikipedia.org/wiki/RT_90
 * 
 * @author Jonas Liljenfeldt, Know IT
 */
public class GaussKrugerProjection implements CoordinateTransformerService {
    // GRS 80 Ellipsoid Characteristics:
    // Semi Major axis
    private static double majorAxis = 6378137.0;
    // Flattening
    private static double flattening = 1.0 / 298.2572221010;

    // RT90 0 gon V 0:-15 fields (Use around Stockholm)
    // Centrum meridian
    private static final String CM_0V = "18D03.2268\"E";
    // Scale factor
    private static final double K0_0V = 1.000005400000;
    // False North
    private static final double FN_0V = -668.844;
    // False East
    private static final double FE_0V = 1500083.521;

    // RT90 2.5 gon V 0:-15 fields (Örebro)
    private static final String CM_25V = "15D48.22624306\"E";
    private static final double K0_25V = 1.00000561024;
    private static final double FN_25V = -667.711;
    private static final double FE_25V = 1500064.274;

    // RT90 5 gon V 0:-15 fields (Malmö)
    private static final String CM_5V = "13D33.376\"E";
    private static final double K0_5V = 1.000005800000;
    private static final double FN_5V = -667.130;
    private static final double FE_5V = 1500044.695;

    // RT90 7.5 gon V 0:-15 fields (Göteborg)
    private static final String CM_75V = "11D18.375\"E";
    private static final double K0_75V = 1.000006000000;
    private static final double FN_75V = -667.282;
    private static final double FE_75V = 1500025.141;

    // RT90 2.5 gon O 0:-15 fields (Umeå)
    private static final String CM_25O = "20D18.379\"E";
    private static final double K0_25O = 1.000005200000;
    private static final double FN_25O = -670.706;
    private static final double FE_25O = 1500102.765;

    // RT90 5 gon O 0:-15 fields (Luleå)
    private static final String CM_5O = "22D33.380\"E";
    private static final double K0_5O = 1.000004900000;
    private static final double FN_5O = -672.557;
    private static final double FE_5O = 1500121.846;

    // Variables
    private String cm;
    private double k0;
    private double fn;
    private double fe;
    // Geodetic latitude
    private double lat;
    // Geodetic longitude
    private double lon;
    // Gauss-Krüger Projection variables
    private double a;
    private double b;
    private double c;
    private double d;
    private double beta1;
    private double beta2;
    private double beta3;
    private double beta4;
    private double e2;
    private double n;
    private double aHat;
    private double delta1;
    private double delta2;
    private double delta3;
    private double delta4;
    private double aStar;
    private double bStar;
    private double cStar;
    private double dStar;

    // RT90 coordinates
    private double rt90X;
    private double rt90Y;

    // WGS84 coordinates

    /**
     * Constructor with specified gon.
     * 
     * @param gon Specified Gon
     * @throws IllegalArgumentException If an unknown Gon is specified.
     */
    public GaussKrugerProjection(final String gon) {
        if (gon != null) {
            if (gon.equals("2.5V")) {
                this.cm = CM_25V;
                this.k0 = K0_25V;
                this.fn = FN_25V;
                this.fe = FE_25V;
            } else if (gon.equals("5V")) {
                this.cm = CM_5V;
                this.k0 = K0_5V;
                this.fn = FN_5V;
                this.fe = FE_5V;
            } else if (gon.equals("7.5V")) {
                this.cm = CM_75V;
                this.k0 = K0_75V;
                this.fn = FN_75V;
                this.fe = FE_75V;
            } else if (gon.equals("0V")) {
                this.cm = CM_0V;
                this.k0 = K0_0V;
                this.fn = FN_0V;
                this.fe = FE_0V;
            } else if (gon.equals("2.50")) {
                this.cm = CM_25O;
                this.k0 = K0_25O;
                this.fn = FN_25O;
                this.fe = FE_25O;
            } else if (gon.equals("50")) {
                this.cm = CM_5O;
                this.k0 = K0_5O;
                this.fn = FN_5O;
                this.fe = FE_5O;
            } else {
                throw new IllegalArgumentException("Specified Gon isn't recognized: " + gon);
            }
        } else {
            throw new IllegalArgumentException("Specified Gon is null");
        }
        this.initialize();
    }

    /**
     * Default constructor using 2.5 V 0:-15 (as in standard RT90).
     */
    public GaussKrugerProjection() {
        // USE 2.5 V 0:-15 as default
        this.cm = CM_25V;
        this.k0 = K0_25V;
        this.fn = FN_25V;
        this.fe = FE_25V;
        this.initialize();
    }

    /**
     * Calculate constants.
     */
    private void initialize() {
        this.e2 = flattening * (2.0 - flattening);
        this.n = flattening / (2.0 - flattening);
        this.aHat = majorAxis / (1.0 + this.n) * (1.0 + 0.25 * Math.pow(this.n, 2) + 1.0 / 64.0 * Math.pow(this.n, 4));
        this.a = this.e2;
        this.b = 1.0 / 6.0 * (5.0 * Math.pow(this.a, 2) - Math.pow(this.a, 3));
        this.c = 1.0 / 120.0 * (104.0 * Math.pow(this.a, 3) - 45.0 * Math.pow(this.a, 4));
        this.d = 1.0 / 1260.0 * 1237.0 * Math.pow(this.a, 4);

        this.beta1 = 0.5 * this.n - 2.0 / 3.0 * Math.pow(this.n, 2) + 5.0 / 16.0 * Math.pow(this.n, 3) + 41.0 / 180.0
                * Math.pow(this.n, 4);
        this.beta2 = 13.0 / 48.0 * Math.pow(this.n, 2) - 3.0 / 5.0 * Math.pow(this.n, 3) + 557.0 / 1440.0
                * Math.pow(this.n, 4);
        this.beta3 = 61.0 / 240.0 * Math.pow(this.n, 3) - 103.0 / 140.0 * Math.pow(this.n, 4);
        this.beta4 = 49561.0 / 161280.0 * Math.pow(this.n, 4);

        this.delta1 = 1.0 / 2.0 * this.n - 2.0 / 3.0 * Math.pow(this.n, 2) + 37.0 / 96.0 * Math.pow(this.a, 3) - 1.0
                / 360.0 * Math.pow(this.n, 4);
        this.delta2 = 1.0 / 48.0 * Math.pow(this.n, 2) + 1.0 / 15.0 * Math.pow(this.n, 3) - 437.0 / 1440.0
                * Math.pow(this.n, 4);
        this.delta3 = 17.0 / 480.0 * Math.pow(this.n, 3) - 37.0 / 840.0 * Math.pow(this.n, 4);
        this.delta4 = 4397.0 / 161280.0 * Math.pow(this.n, 4);

        this.aStar = this.e2 + Math.pow(this.e2, 2) + Math.pow(this.e2, 3) + Math.pow(this.e2, 4);
        this.bStar = -(1.0 / 6.0)
                * (7 * Math.pow(this.e2, 2) + 17.0 * Math.pow(this.e2, 3) + 30 * Math.pow(this.e2, 4));
        this.cStar = 1.0 / 120.0 * (224 * Math.pow(this.e2, 3) + 889.0 * Math.pow(this.e2, 4));
        this.dStar = -(1.0 / 1260.0) * 4279.0 * Math.pow(this.e2, 4);
    }

    /**
     * Calculate grid coordinates with Gauss-Kruger projection method.
     * 
     * @param latitude Latitude in decimal format.
     * @param longitude Longitude in decimal format.
     */
    private void calcGaussKrugerProjectionFromGeodeticToGrid(final double latitude, final double longitude) {
        // Compute the Conformal Latitude
        double phiStar = latitude
                - Math.sin(latitude)
                * Math.cos(latitude)
                * (this.a + this.b * Math.pow(Math.sin(latitude), 2) + this.c * Math.pow(Math.sin(latitude), 4) + this.d
                        * Math.pow(Math.sin(latitude), 6));

        // Difference in longitude
        double dLon = longitude - getLatLongRadiansDecimal(this.cm, true);

        // Get Angles:
        double chi = Math.atan(Math.tan(phiStar) / Math.cos(dLon));

        // Since Atanh isn't represented in the Math-class
        // we'll use a simplification that holds for real z < 1
        // Ref:
        // http://mathworld.wolfram.com/InverseHyperbolicTangent.html
        double z = Math.cos(phiStar) * Math.sin(dLon);
        double eta = 0.5 * Math.log((1.0 + z) / (1.0 - z));

        // Calculate the carthesian (grid) coordinates in RT90
        this.rt90X = this.k0
                * this.aHat
                * (chi + this.beta1 * Math.sin(2.0 * chi) * Math.cosh(2.0 * eta) + this.beta2 * Math.sin(4.0 * chi)
                        * Math.cosh(4.0 * eta) + this.beta3 * Math.sin(6.0 * chi) * Math.cosh(6.0 * eta) + this.beta4
                        * Math.sin(8.0 * chi) * Math.cosh(8.0 * eta)) + this.fn;

        this.rt90Y = this.k0
                * this.aHat
                * (eta + this.beta1 * Math.cos(2.0 * chi) * Math.sinh(2.0 * eta) + this.beta2 * Math.cos(4.0 * chi)
                        * Math.sinh(4.0 * eta) + this.beta3 * Math.cos(6.0 * chi) * Math.sinh(6.0 * eta) + this.beta4
                        * Math.cos(8.0 * chi) * Math.sinh(8.0 * eta)) + this.fe;
    }

    /**
     * Calculate grid coordinates with Gauss-Kruger projection method.
     * 
     * @param x Latitude in RT 90.
     * @param y Longitude in RT 90
     */
    private void calcGaussKrugerProjectionFromGridToGeodetic(final int x, final int y) {
        double chi = (x - this.fn) / (this.k0 * this.aHat);
        double eta = (y - this.fe) / (this.k0 * this.aHat);

        double chiPrim = chi - this.delta1 * Math.sin(2 * chi) * Math.cosh(2 * eta) - this.delta2 * Math.sin(4 * chi)
                * Math.cosh(4 * eta) - this.delta3 * Math.sin(6 * chi) * Math.cosh(6 * eta) - this.delta4
                * Math.sin(8 * chi) * Math.cosh(8 * eta);

        double etaPrim = eta - this.delta1 * Math.cos(2 * chi) * Math.sinh(2 * eta) - this.delta2 * Math.cos(4 * chi)
                * Math.sinh(4 * eta) - this.delta3 * Math.cos(6 * chi) * Math.sinh(6 * eta) - this.delta4
                * Math.cos(8 * chi) * Math.sinh(8 * eta);

        // Compute the Conformal Latitude
        double phiStar = Math.asin(Math.sin(chiPrim) / Math.cosh(etaPrim));

        // Difference in longitude
        double dLon = Math.atan(Math.sinh(etaPrim) / Math.cos(chiPrim));

        // Eventually the latitude and longitude angles are calculated.
        this.lon = getLatLongRadiansDecimal(this.cm, true) + dLon;

        this.lat = phiStar
                + Math.sin(phiStar)
                * Math.cos(phiStar)
                * (this.aStar + this.bStar * Math.pow(Math.sin(phiStar), 2) + this.cStar
                        * Math.pow(Math.sin(phiStar), 4) + this.dStar * Math.pow(Math.sin(phiStar), 6));
    }

    /**
     * {@inheritDoc}
     */
    public int[] getRT90(final double latitude, final double longitude) {
        this.lat = latitude;
        this.lon = longitude;

        // Degrees -> radians
        this.lat = latitude * Math.PI / 180.0;
        this.lon = longitude * Math.PI / 180.0;

        // Calculate Projection on the RT90-grid
        this.calcGaussKrugerProjectionFromGeodeticToGrid(this.lat, this.lon);

        // Return x,y array. Not nice conversion but works :)
        int[] rt90Coordinates = { Integer.parseInt(String.valueOf(Math.round(this.rt90X))),
                Integer.parseInt(String.valueOf(Math.round(this.rt90Y))) };
        return rt90Coordinates;
    }

    /**
     * {@inheritDoc}
     */
    public double[] getWGS84(final int x, final int y) {
        this.rt90X = x;
        this.rt90Y = y;

        // Calculate geodetic coordinates from RT90 coordinates
        this.calcGaussKrugerProjectionFromGridToGeodetic(Integer.parseInt(String.valueOf(Math.round(this.rt90X))),
                Integer.parseInt(String.valueOf(Math.round(this.rt90Y))));

        // Convert WGS84 coordinates from radians to decimal degrees
        double wgs84LatDegree = this.lat / (Math.PI / 180.0);
        double wgs84LonDegree = this.lon / (Math.PI / 180.0);

        // Return lat, lon array
        double[] wgs84Coordinates = { wgs84LatDegree, wgs84LonDegree };
        return wgs84Coordinates;
    }

    /**
     * Parse NMEA-String.
     * 
     * @param latOrLong Latitude or longitude in NMEA format
     * @param isLong True if longitude, false if latitude.
     * @return Latitude or longitude in radians as decimal value.
     */
    static double getLatLongRadiansDecimal(final String latOrLong, final boolean isLong) {
        // Get Hours (up to the 'D')
        double deciLatLon = Double.parseDouble(latOrLong.substring(0, latOrLong.indexOf("D")));

        // Remove it once we've used it
        String remainder = latOrLong.substring(latOrLong.indexOf("D") + 1);

        // Get Minutes (up to the '.') and3872648 divide by Minutes/Hour
        deciLatLon += Double.parseDouble(remainder.substring(0, remainder.indexOf("."))) / 60.0;

        // Remove it once we've used it
        remainder = remainder.substring(remainder.indexOf(".") + 1);

        // Get Seconds (up to the '"') and divide by Seconds/Hour
        String sec = remainder.substring(0, remainder.indexOf("\""));
        // Insert a dot to prevent the time from flying away...
        deciLatLon += Double.parseDouble(new StringBuilder(sec).insert(2, ".").toString()) / 3600.0;

        // Get the Hemisphere String
        remainder = remainder.substring(remainder.indexOf("\"") + 1);
        if (isLong && "S".equals(remainder) || !isLong && "W".equals(remainder)) {
            // Set us right
            deciLatLon = -deciLatLon;
        }
        // And return (as radians)
        return deciLatLon * Math.PI / 180.0;
    }
}