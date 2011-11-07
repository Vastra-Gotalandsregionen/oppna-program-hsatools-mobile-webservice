package se.knowit.mobile.hriv.ws;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import se.knowit.mobile.hriv.ws.domain.KivwsUnitMapper;
import se.knowit.mobile.hriv.ws.domain.Unit;

public class HrivWebServiceTest {

	private HrivWebService hrivWebService;
	private WebserviceFake webserviceMockFactory;
	private KivwsUnitMapper kivwsUnitMapper;

	@Before
	public void setUp() throws Exception {
		hrivWebService = new HrivWebService();
		webserviceMockFactory = new WebserviceFake();
		kivwsUnitMapper = new KivwsUnitMapper();
		webserviceMockFactory.setKivwsUnitMapp(kivwsUnitMapper);
		hrivWebService.setKivwsSearchService(webserviceMockFactory);
		//hrivWebService.setKivwsUnitMapper(kivwsUnitMapper);
		//hrivWebService.setRegionWebService(webserviceMockFactory);
	}

	@Test
	public void testGetCareUnits() {
		ModelAndView careUnits = hrivWebService.getCareUnits();
		List<Unit> result = (List<Unit>) careUnits.getModelMap().get("careUnits");
		assertEquals(207, result.size());
	}

	@Test
	public void testGetEmergencyUnits() {
		ModelAndView careUnits = hrivWebService.getEmergencyUnits();
		List<Unit> result = (List<Unit>) careUnits.getModelMap().get("emergencyUnits");		
		assertEquals(9, result.size()); // Filtered from 80 to 10 in #getEmergencyUnits(), Auktmottagning, Lidking - SE2321000131-E000000003019 is missing in the test data
	}

	@Test
	public void testGetDutyUnits() {
		ModelAndView careUnits = hrivWebService.getDutyUnits();
		List<Unit> result = (List<Unit>) careUnits.getModelMap().get("dutyUnits");
		assertEquals(24, result.size());
	}

}
