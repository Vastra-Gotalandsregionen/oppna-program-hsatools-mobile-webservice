package se.knowit.mobile.hriv.ws.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.knowit.mobile.hriv.ws.kivws.Function;
import se.knowit.mobile.hriv.ws.kivws.String2ArrayOfAnyTypeMap.Entry;
import se.knowit.mobile.hriv.ws.utils.CoordinateTransformerService;
import se.knowit.mobile.hriv.ws.utils.GaussKrugerProjection;
import se.knowit.mobile.hriv.ws.utils.GeoUtil;

/**
 * Mapp kivws response object to Unit.
 * 
 * @author david bennehult
 * 
 */
public class KivwsUnitMapper {

  private Map<String, List<Object>> ldapAttributes;

  public Unit mapFromContext(se.knowit.mobile.hriv.ws.kivws.Unit kivwsUnit) {
    Unit unit = new Unit();
    ldapAttributes = new HashMap<String, List<Object>>();
    List<Entry> attributes = kivwsUnit.getAttributes().getValue().getEntry();

    for (Entry entry : attributes) {
      ldapAttributes.put(entry.getKey(), entry.getValue().getAnyType());
    }
    populateGeoCoordinates(unit);
    // HsaIdentity.
    unit.setHsaIdentity(getSingleValue(KivwsAttributes.HSA_IDENTITY));
    // Unit name.
    populateUnitName(unit);
    // Unit locale (l attribute).
    unit.setLocale(getSingleValue(KivwsAttributes.L));
    // HsaSurgeryHours.
    List<String> visitingHours = getMultiValue(KivwsAttributes.HSA_SURGERY_HOURS);
    unit.setHsaSurgeryHours(createTimeInterval(visitingHours));
    // HsaDropInHours.
    List<String> hsaDropInHours = getMultiValue(KivwsAttributes.HSA_DROPIN_HOURS);
    unit.setHsaDropInHours(createTimeInterval(hsaDropInHours));
    // HsaManagementCodeText (Drivs av).
    unit.setHsaManagementCodeText(getSingleValue(KivwsAttributes.HSA_MANAGEMENT_CODE_TEXT));
    // HsaPublicTelephoneNumber.
    unit.setHsaPublicTelephoneNumber(getSingleValue(KivwsAttributes.HSA_PUBLIC_TELEPHONE_NUMBER));
    // HsaTelephoneTime.
    List<String> telephoneTime = getMultiValue(KivwsAttributes.HSA_TELEPHONE_TIME);
    unit.setHsaTelephoneTime(createTimeInterval(telephoneTime));
    // HsaVisitingRuleAge
    unit.setHsaVisitingRuleAge(getSingleValue(KivwsAttributes.HSA_VISITING_RULE_AGE));
    // HsaLabeledUri.
    unit.setLabeleduri(getSingleValue(KivwsAttributes.LABELED_URI));
    // Description
    unit.setDescription(getSingleValue(KivwsAttributes.DESCRIPTION));
    
    return unit;
  }
  
  private void populateUnitName(Unit unit) {
	    // Name
	    String ou = getSingleValue(KivwsAttributes.OU);
	    String unitName;
	    if (ou!=null && ou.length()>0) {
	    	
	      unitName = ou.replace("\\,", ",");
	      unit.setName(unitName.trim());
	    } else {
	      String cn = getSingleValue(KivwsAttributes.CN);
	      // change \, to ,
	      unitName = cn.replace("\\,", ",");
	      unit.setName(unitName.trim());
	    }
	  }

  private String getSingleValue(String key) {
    String returnValue = "";
    if (ldapAttributes.containsKey(key)) {
      returnValue = (String) ldapAttributes.get(key).get(0);
    }
    return returnValue;
  }

  private List<String> getMultiValue(String key) {
    List<String> result = new ArrayList<String>();
    List<Object> values = ldapAttributes.get(key);
    if (ldapAttributes.containsKey(key)) {
      for (Object object : values) {
        result.add((String) object);
      }
    }
    return result;
  }

  private void populateGeoCoordinates(Unit unit) {
    // Coordinates
    if ((String) getSingleValue(KivwsAttributes.HSA_GEOGRAPHICAL_COORDINATES) != null) {
      String hsaGeographicalCoordinates = (String) getSingleValue(KivwsAttributes.HSA_GEOGRAPHICAL_COORDINATES);
      // Parse and set in RT90 format
      int[] rt90Coords = GeoUtil.parseRT90HsaString(hsaGeographicalCoordinates);
      if (rt90Coords != null) {
        // Convert to WGS84 and set on unit too
        CoordinateTransformerService gkp = new GaussKrugerProjection("2.5V");
        double[] wgs84Coords = gkp.getWGS84(rt90Coords[0], rt90Coords[1]);
        unit.setLatitude(wgs84Coords[0]);
        unit.setLongitude(wgs84Coords[1]);
      }
    }
  }

  private Map<String, List<OfficeHours>> createTimeInterval(List<String> timeInterval) {
    Map<String, List<OfficeHours>> officeHours = new HashMap<String, List<OfficeHours>>();
    List<WeekdayTime> createWeekdayTimeList = WeekdayTime.createWeekdayTimeList(timeInterval);
    for (WeekdayTime weekdayTime : createWeekdayTimeList) {
      int startDay = weekdayTime.getStartDay();
      int endDay = weekdayTime.getEndDay();
      int i = startDay;
      do {
        getListForKey(officeHours, WeekdayTime.getDayName(i)).add(new OfficeHours(weekdayTime.getStartTimeDisplayValue(), weekdayTime.getEndTimeDisplayValue()));
        // officeHours.put(WeekdayTime.getDayName(i), new OfficeHours(weekdayTime.getStartTimeDisplayValue(), weekdayTime.getEndTimeDisplayValue()));
        i++;
      } while (i <= endDay);

    }
    return officeHours;
  }

  private List<OfficeHours> getListForKey(Map<String, List<OfficeHours>> officeHours, String key) {
    List<OfficeHours> officeHoursList = officeHours.get(key);
    if (officeHoursList == null) {
      officeHoursList = new ArrayList<OfficeHours>();
      officeHours.put(key, officeHoursList);
    }
    return officeHoursList;
  }

public Unit mapFromContext(Function function2) {
	Unit unit = new Unit();
    ldapAttributes = new HashMap<String, List<Object>>();
    List<Entry> attributes = function2.getAttributes().getValue().getEntry();

    for (Entry entry : attributes) {
      ldapAttributes.put(entry.getKey(), entry.getValue().getAnyType());
    }
    populateGeoCoordinates(unit);
    // HsaIdentity.
    unit.setHsaIdentity(getSingleValue(KivwsAttributes.HSA_IDENTITY));
    // Unit name.
   populateUnitName(unit);
    // Unit locale (l attribute).
    unit.setLocale(getSingleValue(KivwsAttributes.L));
    // HsaSurgeryHours.
    List<String> visitingHours = getMultiValue(KivwsAttributes.HSA_SURGERY_HOURS);
    unit.setHsaSurgeryHours(createTimeInterval(visitingHours));
    // HsaDropInHours.
    List<String> hsaDropInHours = getMultiValue(KivwsAttributes.HSA_DROPIN_HOURS);
    unit.setHsaDropInHours(createTimeInterval(hsaDropInHours));
    // HsaManagementCodeText (Drivs av).
    unit.setHsaManagementCodeText(getSingleValue(KivwsAttributes.HSA_MANAGEMENT_CODE_TEXT));
    // HsaPublicTelephoneNumber.
    unit.setHsaPublicTelephoneNumber(getSingleValue(KivwsAttributes.HSA_PUBLIC_TELEPHONE_NUMBER));
    // HsaTelephoneTime.
    List<String> telephoneTime = getMultiValue(KivwsAttributes.HSA_TELEPHONE_TIME);
    unit.setHsaTelephoneTime(createTimeInterval(telephoneTime));
    // HsaVisitingRuleAge
    unit.setHsaVisitingRuleAge(getSingleValue(KivwsAttributes.HSA_VISITING_RULE_AGE));
    // HsaLabeledUri.
    unit.setLabeleduri(getSingleValue(KivwsAttributes.LABELED_URI));
    // Description
    unit.setDescription(getSingleValue(KivwsAttributes.DESCRIPTION));
    
    return unit;
  
	}
}
