package se.knowit.mobile.hriv.ws.domain;

public class OfficeHours {

  private String openingHour;
  private String closingHour;

  public OfficeHours(String openingHour, String closingHour) {
    super();
    this.openingHour = openingHour;
    this.closingHour = closingHour;
  }

  public String getOpeningHour() {
    return openingHour;
  }

  public void setOpeningHour(String openingHour) {
    this.openingHour = openingHour;
  }

  public String getClosingHour() {
    return closingHour;
  }

  public void setClosingHour(String closingHour) {
    this.closingHour = closingHour;
  }

}
