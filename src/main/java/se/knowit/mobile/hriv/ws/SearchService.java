package se.knowit.mobile.hriv.ws;

import java.util.List;

import se.knowit.mobile.hriv.ws.domain.Unit;

public interface SearchService {
	
	public List<Unit> searchUnits(String filter, int searchScope, List<String> attrs);

}
