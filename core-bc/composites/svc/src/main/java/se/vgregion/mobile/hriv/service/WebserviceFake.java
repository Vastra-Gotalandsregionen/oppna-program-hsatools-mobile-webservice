package se.vgregion.mobile.hriv.service;

import com.thoughtworks.xstream.XStream;
import org.springframework.core.io.ClassPathResource;
import se.vgregion.mobile.hriv.utils.KivwsUnitMapper;
import se.vgregion.mobile.hriv.kivws.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class WebserviceFake implements SearchService {

	private ArrayOfUnit kivwsCareUnits;
	private ArrayOfUnit kivwsEmergencyUnits;
	private ArrayOfUnit kivwsDutyUnits;
	private KivwsUnitMapper kivwsUnitMapper;

	public void setKivwsUnitMapp(KivwsUnitMapper kivwsUnitMapper) {
		this.kivwsUnitMapper = kivwsUnitMapper;
	}

	public WebserviceFake() {
		try {
			ClassPathResource kivwsCareUnitsResource = new ClassPathResource(
					"careUnits.xml");
			ClassPathResource kivwsEmergencyUnitsResource = new ClassPathResource(
					"emergencyUnits.xml");
			ClassPathResource kivwsDutyUnitsResource = new ClassPathResource(
					"dutyUnits.xml");
			XStream xStream = new XStream();
			ObjectInputStream kivwsCareInputStream = xStream
					.createObjectInputStream(kivwsCareUnitsResource
							.getInputStream());
			ObjectInputStream kivwsEmergencyInputStream = xStream
					.createObjectInputStream(kivwsEmergencyUnitsResource
							.getInputStream());
			ObjectInputStream kivwsDutyInputStream = xStream
					.createObjectInputStream(kivwsDutyUnitsResource
							.getInputStream());
			kivwsCareUnits = (ArrayOfUnit) kivwsCareInputStream.readObject();
			kivwsEmergencyUnits = (ArrayOfUnit) kivwsEmergencyInputStream
					.readObject();
			kivwsDutyUnits = (ArrayOfUnit) kivwsDutyInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public ArrayOfUnit searchUnit(String arg0, ArrayOfString arg1,
			VGRegionDirectory arg2, String arg3) throws VGRException_Exception {
		ArrayOfUnit arrayOfUnit = null;
		if (SearchService.CAREUNITS_LDAP_QUREY.equals(arg0)) {
			arrayOfUnit = kivwsCareUnits;

		} else if (SearchService.EMERGENCYUNITS_LDAP_QUREY.equals(arg0)) {
			arrayOfUnit = kivwsEmergencyUnits;
		} else if (SearchService.DUTYUNITS_LDAP_QUREY.equals(arg0)) {
			arrayOfUnit = kivwsDutyUnits;
		}
		return arrayOfUnit;
	}

	@Override
	public List<se.vgregion.mobile.hriv.domain.Unit> searchUnits(
			String filter, int searchScope, List<String> attrs) {
		ArrayList<se.vgregion.mobile.hriv.domain.Unit> result = new ArrayList<se.vgregion.mobile.hriv.domain.Unit>();
		try {
			ArrayOfUnit searchUnit = searchUnit(filter, null, null, null);
			for (Unit unit : searchUnit.getUnit()) {
				result.add(kivwsUnitMapper.mapFromContext(unit));
			}

		} catch (VGRException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
