package se.knowit.mobile.hriv.ws;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.knowit.mobile.hriv.ws.domain.KivwsUnitMapper;
import se.knowit.mobile.hriv.ws.domain.Unit;
import se.knowit.mobile.hriv.ws.kivws.ArrayOfFunction;
import se.knowit.mobile.hriv.ws.kivws.ArrayOfString;
import se.knowit.mobile.hriv.ws.kivws.ArrayOfUnit;
import se.knowit.mobile.hriv.ws.kivws.Function;
import se.knowit.mobile.hriv.ws.kivws.VGRException_Exception;
import se.knowit.mobile.hriv.ws.kivws.VGRegionDirectory;
import se.knowit.mobile.hriv.ws.kivws.VGRegionWebServiceImplPortType;


public class KivwsSearchService implements SearchService{

  private VGRegionWebServiceImplPortType vgregionWebService;
  private KivwsUnitMapper kivwsUnitMapper;
  private static final String base ="ou=Org,o=vgr";
  private final Log logger = LogFactory.getLog(KivwsSearchService.class);

  public void setKivwsUnitMapper(KivwsUnitMapper kivwsUnitMapper) {
    this.kivwsUnitMapper = kivwsUnitMapper;
  }

  public void setVgregionWebService(VGRegionWebServiceImplPortType vgregionWebService) {
    this.vgregionWebService = vgregionWebService;
  }

 
  private List<Unit> searchFunctionUnits(String filter, int searchScope, List<String> attrs) {
    List<Unit> resultUnits = new ArrayList<Unit>();
    ArrayOfString arrayOfString = new ArrayOfString();
    arrayOfString.getString().addAll(attrs);
    try {
      ArrayOfFunction searchFunction = vgregionWebService.searchFunction(filter, arrayOfString, VGRegionDirectory.KIV, base, Integer.toString(searchScope));
      resultUnits.addAll(this.mapKivwsUnits(searchFunction));
    } catch (VGRException_Exception e) {
      logger.error(e.getMessage(), e);
    }
    return resultUnits;
  }
  
  @Override
  public List<Unit> searchUnits(String filter, int searchScope, List<String> attrs) {
    List<Unit> resultUnits = null;
    List<Unit> result = new ArrayList<Unit>();
    ArrayOfString arrayOfString = new ArrayOfString();
    arrayOfString.getString().addAll(attrs);

    try {
      ArrayOfUnit searchUnit = vgregionWebService.searchUnit(filter, arrayOfString, VGRegionDirectory.KIV, base, Integer.toString(searchScope));
      resultUnits = mapKivwsUnits(searchUnit);
      List<Unit> resultFunctions = searchFunctionUnits(filter, searchScope, attrs);
      result.addAll(resultUnits);
      result.addAll(resultFunctions);
    } catch (VGRException_Exception e) {
      logger.error(e.getMessage(), e);
      resultUnits = new ArrayList<Unit>();
    }
    System.out.println("Size: " + result.size());
    return result;
  }

  private List<Unit> mapKivwsUnits(Object object) {
    List<Unit> result = null;
    if (object instanceof ArrayOfFunction) {
      result = mapKivwsUnitFunctionToUnit((ArrayOfFunction) object);
    } else {
      result = mapKivwsUnitToUnit((ArrayOfUnit) object);
    }
    return result;
  }

  private List<Unit> mapKivwsUnitToUnit(ArrayOfUnit arrayOfUnit) {
    List<se.knowit.mobile.hriv.ws.kivws.Unit> unit = arrayOfUnit.getUnit();
    List<Unit> result = new ArrayList<Unit>();
    for (se.knowit.mobile.hriv.ws.kivws.Unit kivwsUnit : unit) {
      result.add(kivwsUnitMapper.mapFromContext(kivwsUnit));
    }
    return result;
  }

  private List<Unit> mapKivwsUnitFunctionToUnit(ArrayOfFunction arrayOfFunction) {
    List<Unit> result = new ArrayList<Unit>();
    List<Function> function = arrayOfFunction.getFunction();
    for (Function function2 : function) {
      result.add(kivwsUnitMapper.mapFromContext(function2));
    }
    return result;
  }
}
