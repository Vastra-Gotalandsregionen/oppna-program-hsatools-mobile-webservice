package se.knowit.mobile.hriv.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import se.knowit.mobile.hriv.ws.domain.Unit;

@Controller
public class HrivWebService {

	public static final String EMERGENCYUNITS_LDAP_QUREY = "(&(&(&(|(vgrcaretype=01))(|(hsabusinessclassificationcode=1000)(hsabusinessclassificationcode=1100)(hsabusinessclassificationcode=1500)(hsabusinessclassificationcode=1600)(hsabusinessclassificationcode=1800)(hsabusinessclassificationcode=1801)(hsabusinessclassificationcode=1812))))(hsaDestinationIndicator=03))";
	public static final String CAREUNITS_LDAP_QUREY = "(&(&(&(|(hsabusinesstype=02))))(hsaDestinationIndicator=03))";
	public static final String DUTYUNITS_LDAP_QUREY = "(&(&(&(|(hsabusinessclassificationcode=1500))))(hsaDestinationIndicator=03))";


	private SearchService kivwsSearchService;

	public enum vgrCareType {
		CAREUNITS, EMERGENCYUNITS, DUTYUNITS
	}

	// Vårdmottagning
	@RequestMapping("/getCareUnits")
	public ModelAndView getCareUnits() {
		List<Unit> listOfUnits = getUnits(vgrCareType.CAREUNITS);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("careUnits", listOfUnits);
		return modelAndView;
	}

	// Akutmottagning
	@RequestMapping("/getEmergencyUnits")
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
		
		List<Unit> listOfUnits = getUnits(vgrCareType.EMERGENCYUNITS);
		List<Unit> listOfFilteredEmergencyUnits = new ArrayList<Unit>();
		for (Unit unit : listOfUnits) {
			String hsaIdentitiy = unit.getHsaIdentity();
			if(emergencyHsaIdentities.contains(hsaIdentitiy)) {
				listOfFilteredEmergencyUnits.add(unit);
			}
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("emergencyUnits", listOfFilteredEmergencyUnits);
		return modelAndView;
	}

	// Jourmottagning
	@RequestMapping("/getDutyUnits")
	public ModelAndView getDutyUnits() {
		List<Unit> listOfUnits = getUnits(vgrCareType.DUTYUNITS);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("dutyUnits", listOfUnits);
		return modelAndView;
	}

	private List<Unit> getUnits(vgrCareType careType) {
		switch (careType) {
		case CAREUNITS:
			return getHsaUnit(CAREUNITS_LDAP_QUREY);
		case EMERGENCYUNITS:
			return getHsaUnit(EMERGENCYUNITS_LDAP_QUREY);
		case DUTYUNITS:
			return getHsaUnit(DUTYUNITS_LDAP_QUREY);
		default:
			break;
		}
		return null;
	}

	private List<Unit> getHsaUnit(String unitSearch) {
		List<Unit> resultUnits = new ArrayList<Unit>();
		resultUnits = kivwsSearchService.searchUnits(unitSearch, 2, new ArrayList<String>());
		return resultUnits;
	}

	@Autowired
	public void setKivwsSearchService(SearchService kivwsSearchService) {
		this.kivwsSearchService = kivwsSearchService;
	}
}
