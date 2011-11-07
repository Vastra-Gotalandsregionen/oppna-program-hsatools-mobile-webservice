package se.knowit.mobile.hriv.ws;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.knowit.mobile.hriv.ws.domain.KivwsUnitMapper;
import se.knowit.mobile.hriv.ws.kivws.ArrayOfString;
import se.knowit.mobile.hriv.ws.kivws.ArrayOfUnit;
import se.knowit.mobile.hriv.ws.kivws.Unit;
import se.knowit.mobile.hriv.ws.kivws.VGRException_Exception;
import se.knowit.mobile.hriv.ws.kivws.VGRegionDirectory;

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
    
    ArrayOfUnit searchUnit = webserviceFake.searchUnit(HrivWebService.CAREUNITS_LDAP_QUREY, new ArrayOfString(), VGRegionDirectory.KIV, null);
    for (Unit unit : searchUnit.getUnit()) {
      kivwsUnitMapper.mapFromContext(unit);
    }
  }
  
  public void testUnitMapper() throws VGRException_Exception{
    KivwsUnitMapper kivwsUnitMapperFake = new KivwsUnitMapper();
    ArrayOfUnit searchUnit = webserviceFake.searchUnit(HrivWebService.CAREUNITS_LDAP_QUREY, new ArrayOfString(), VGRegionDirectory.KIV, null);
    for (Unit unit : searchUnit.getUnit()) {
      se.knowit.mobile.hriv.ws.domain.Unit mapFromContext = kivwsUnitMapperFake.mapFromContext(unit);
      assertEquals("0-99", mapFromContext.getHsaVisitingRuleAge());
      assertEquals(mapFromContext.getHsaSurgeryHours(),mapFromContext.getHsaTelephoneTime());
    }
  }
}
