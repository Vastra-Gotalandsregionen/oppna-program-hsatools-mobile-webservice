package se.vgregion.mobile.hriv.ws;

import se.vgregion.mobile.hriv.domain.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import se.vgregion.mobile.hriv.service.SearchService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HrivWebService {

    private SearchService kivwsSearchService;

    public enum VgrCareType {
        CAREUNITS, EMERGENCYUNITS, DUTYUNITS
    }

    /**
     * Constructor.
     *
     * @param kivwsSearchService kivwsSearchService
     */
    @Autowired
    public HrivWebService(SearchService kivwsSearchService) {
        this.kivwsSearchService = kivwsSearchService;
    }

    /**
     * Searches for care units.
     *
     * @return the resulting care units in a {@link ModelAndView} object
     */
    // Vårdmottagning
    @RequestMapping({"/getCareUnits", "/getCareUnits/jsonp"})
    public ModelAndView getCareUnits() {
        List<Unit> listOfUnits = getUnits(VgrCareType.CAREUNITS);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("careUnits", listOfUnits);
        return modelAndView;
    }

    /**
     * Searches for emergency units.
     *
     * @return the resulting emergency units in a {@link ModelAndView} object
     */
    // Akutmottagning
    @RequestMapping({"/getEmergencyUnits", "/getEmergencyUnits/jsonp"})
    public ModelAndView getEmergencyUnits() {
        List<String> emergencyHsaIdentities = new ArrayList<String>();
        emergencyHsaIdentities.add("SE2321000131-E000000000757");
        emergencyHsaIdentities.add("SE2321000131-E000000002771");
        emergencyHsaIdentities.add("SE2321000131-E000000002286");
        emergencyHsaIdentities.add("SE2321000131-E000000007305");
        emergencyHsaIdentities.add("SE2321000131-E000000001331");
        emergencyHsaIdentities.add("SE2321000131-E000000003019");
        emergencyHsaIdentities.add("SE2321000131-E000000006019");
        emergencyHsaIdentities.add("SE2321000131-E000000002911");
        emergencyHsaIdentities.add("SE2321000131-E000000006302");
        emergencyHsaIdentities.add("SE2321000131-E000000006335");

        List<Unit> listOfUnits = getUnits(VgrCareType.EMERGENCYUNITS);
        List<Unit> listOfFilteredEmergencyUnits = new ArrayList<Unit>();
        for (Unit unit : listOfUnits) {
            String hsaIdentitiy = unit.getHsaIdentity();
            if (emergencyHsaIdentities.contains(hsaIdentitiy)) {
                listOfFilteredEmergencyUnits.add(unit);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("emergencyUnits", listOfFilteredEmergencyUnits);
        return modelAndView;
    }

    /**
     * Searches for duty units.
     *
     * @return the resulting duty units in a {@link ModelAndView} object
     */
    // Jourmottagning
    @RequestMapping({"/getDutyUnits", "/getDutyUnits/jsonp"})
    public ModelAndView getDutyUnits() {
        List<Unit> listOfUnits = getUnits(VgrCareType.DUTYUNITS);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dutyUnits", listOfUnits);
        return modelAndView;
    }

    private List<Unit> getUnits(VgrCareType careType) {
        switch (careType) {
            case CAREUNITS:
                return getHsaUnit(SearchService.CAREUNITS_LDAP_QUREY);
            case EMERGENCYUNITS:
                return getHsaUnit(SearchService.EMERGENCYUNITS_LDAP_QUREY);
            case DUTYUNITS:
                return getHsaUnit(SearchService.DUTYUNITS_LDAP_QUREY);
            default:
                break;
        }
        return null;
    }

    private List<Unit> getHsaUnit(String unitSearch) {
        List<Unit> resultUnits = kivwsSearchService.searchUnits(unitSearch, 2, new ArrayList<String>());
        return resultUnits;
    }

}
