package se.vgregion.mobile.hriv.service;

import java.util.ArrayList;
import java.util.List;

import se.vgregion.mobile.hriv.utils.KivwsUnitMapper;
import se.vgregion.mobile.hriv.domain.Unit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.vgregion.mobile.hriv.kivws.ArrayOfFunction;
import se.vgregion.mobile.hriv.kivws.ArrayOfString;
import se.vgregion.mobile.hriv.kivws.ArrayOfUnit;
import se.vgregion.mobile.hriv.kivws.Function;
import se.vgregion.mobile.hriv.kivws.VGRException_Exception;
import se.vgregion.mobile.hriv.kivws.VGRegionDirectory;
import se.vgregion.mobile.hriv.kivws.VGRegionWebServiceImplPortType;


public class KivwsSearchService implements SearchService {

    private VGRegionWebServiceImplPortType vgregionWebService;
    private KivwsUnitMapper kivwsUnitMapper;
    private static final String base = "ou=Org,o=vgr";
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
        List<se.vgregion.mobile.hriv.kivws.Unit> unit = arrayOfUnit.getUnit();
        List<Unit> result = new ArrayList<Unit>();
        for (se.vgregion.mobile.hriv.kivws.Unit kivwsUnit : unit) {
            try {
                result.add(kivwsUnitMapper.mapFromContext(kivwsUnit));
            } catch (RuntimeException e) {
                try {
                    logger.error("Mapping failed for unit " + kivwsUnit.getDn().getValue(), e);
                } catch (RuntimeException e1) {
                    // In case of e.g. NullPointerException
                    logger.error("Mapping failed for unit.", e);
                    logger.error(e1.getMessage(), e1);
                }
            }
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
