package se.vgregion.mobile.hriv.service;

import java.util.List;

import se.vgregion.mobile.hriv.domain.Unit;

public interface SearchService {

    String EMERGENCYUNITS_LDAP_QUREY = "(&(&(&(|(vgrcaretype=01))(|(hsabusinessclassificationcode=1000)(hsabusinessclassificationcode=1100)(hsabusinessclassificationcode=1500)(hsabusinessclassificationcode=1600)(hsabusinessclassificationcode=1800)(hsabusinessclassificationcode=1801)(hsabusinessclassificationcode=1812))))(hsaDestinationIndicator=03))";
    String CAREUNITS_LDAP_QUREY = "(&(&(&(|(hsabusinesstype=02))))(hsaDestinationIndicator=03))";
    String DUTYUNITS_LDAP_QUREY = "(&(&(&(|(hsabusinessclassificationcode=1500))))(hsaDestinationIndicator=03))";

	List<Unit> searchUnits(String filter, int searchScope, List<String> attrs);

}
