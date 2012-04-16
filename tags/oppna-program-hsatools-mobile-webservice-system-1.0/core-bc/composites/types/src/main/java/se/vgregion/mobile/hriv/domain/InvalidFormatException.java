package se.vgregion.mobile.hriv.domain;

public class InvalidFormatException extends Exception {

    private static final long serialVersionUID = 273579528325253443L;

    /**
     * Constructor.
     *
     * @param string message
     */
    public InvalidFormatException(String string) {
        super(string);
    }

}
