package se.knowit.mobile.hriv.ws.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit
 * 
 * @author davidbennehult
 * 
 */
public class Unit {

  private double latitude;
  private double longitude;
  private String name;
  private String locale;
  private String hsaPublicTelephoneNumber;
  private Map<String, List<OfficeHours>> hsaTelephoneTime;
  // Färdbeskrivning
  private String hsaRoute;
  private Map<String, List<OfficeHours>> hsaDropInHours;
  private String hsaVisitingRuleAge;
  private String description;
  private Map<String, List<OfficeHours>> hsaSurgeryHours = new HashMap<String, List<OfficeHours>>();
  private String hsaManagementCodeText;
  private String hsaIdentity;
  private String labeleduri;

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public String getHsaIdentity() {
    return hsaIdentity;
  }

  public void setHsaIdentity(String hsaIdentity) {
    this.hsaIdentity = hsaIdentity;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public Map<String, List<OfficeHours>> getHsaTelephoneTime() {
    return hsaTelephoneTime;
  }

  public void setHsaTelephoneTime(Map<String, List<OfficeHours>> hsaTelephoneTime) {
    this.hsaTelephoneTime = hsaTelephoneTime;
  }

  public String getHsaRoute() {
    return hsaRoute;
  }

  public void setHsaRoute(String hsaRoute) {
    this.hsaRoute = hsaRoute;
  }

  public String getHsaVisitingRuleAge() {
    return hsaVisitingRuleAge;
  }

  public void setHsaVisitingRuleAge(String hsaVisitingRuleAge) {
    this.hsaVisitingRuleAge = hsaVisitingRuleAge;
  }

  public Map<String, List<OfficeHours>> getHsaDropInHours() {
    return hsaDropInHours;
  }

  public void setHsaDropInHours(Map<String, List<OfficeHours>> hsaDropInHours) {
    this.hsaDropInHours = hsaDropInHours;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, List<OfficeHours>> getHsaSurgeryHours() {
    return hsaSurgeryHours;
  }

  public void setHsaSurgeryHours(Map<String, List<OfficeHours>> map) {
    this.hsaSurgeryHours = map;
  }

  public String getHsaManagementCodeText() {
    return hsaManagementCodeText;
  }

  public void setHsaManagementCodeText(String hsaManagementCodeText) {
    this.hsaManagementCodeText = hsaManagementCodeText;
  }

  public void setLabeleduri(String labeleduri) {
    this.labeleduri = labeleduri;
  }

  public String getLabeleduri() {
    return labeleduri;
  }

  public String getHsaPublicTelephoneNumber() {
    return hsaPublicTelephoneNumber;
  }

  public void setHsaPublicTelephoneNumber(String hsaPublicTelephoneNumber) {
    this.hsaPublicTelephoneNumber = hsaPublicTelephoneNumber;
  }

}
