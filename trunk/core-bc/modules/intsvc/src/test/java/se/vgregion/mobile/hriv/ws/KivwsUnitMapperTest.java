package se.vgregion.mobile.hriv.ws;

import static org.junit.Assert.*;

import se.vgregion.mobile.hriv.utils.KivwsUnitMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.vgregion.mobile.hriv.kivws.ArrayOfString;
import se.vgregion.mobile.hriv.kivws.ArrayOfUnit;
import se.vgregion.mobile.hriv.kivws.Unit;
import se.vgregion.mobile.hriv.kivws.VGRException_Exception;
import se.vgregion.mobile.hriv.kivws.VGRegionDirectory;
import se.vgregion.mobile.hriv.service.SearchService;
import se.vgregion.mobile.hriv.service.WebserviceFake;

public class KivwsUnitMapperTest {

    private static WebserviceFake webserviceFake;
    private KivwsUnitMapper kivwsUnitMapper;

    @BeforeClass
    public static void setupBeforeClass() {
        webserviceFake = new WebserviceFake();

    }

    @Before
    public void setUp() throws Exception {
        kivwsUnitMapper = new KivwsUnitMapper();
    }

    @Test
    public void testMapFromContext() throws VGRException_Exception {

        ArrayOfUnit searchUnit = webserviceFake.searchUnit(SearchService.CAREUNITS_LDAP_QUREY, new ArrayOfString(), VGRegionDirectory.KIV, null);
        for (Unit unit : searchUnit.getUnit()) {
            kivwsUnitMapper.mapFromContext(unit);
        }
    }

    public void testUnitMapper() throws VGRException_Exception {
        KivwsUnitMapper kivwsUnitMapperFake = new KivwsUnitMapper();
        ArrayOfUnit searchUnit = webserviceFake.searchUnit(SearchService.CAREUNITS_LDAP_QUREY, new ArrayOfString(), VGRegionDirectory.KIV, null);
        for (Unit unit : searchUnit.getUnit()) {
            se.vgregion.mobile.hriv.domain.Unit mapFromContext = kivwsUnitMapperFake.mapFromContext(unit);
            assertEquals("0-99", mapFromContext.getHsaVisitingRuleAge());
            assertEquals(mapFromContext.getHsaSurgeryHours(), mapFromContext.getHsaTelephoneTime());
        }
    }
}
