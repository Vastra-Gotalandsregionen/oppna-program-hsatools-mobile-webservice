package se.knowit.mobile.hriv.ws;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.xml.ws.BindingProvider;
import se.knowit.mobile.hriv.ws.kivws.VGRegionWebServiceImpl;
import se.knowit.mobile.hriv.ws.kivws.VGRegionWebServiceImplPortType;

public class KivwsFactoryBean {

  private static final String USERNAME = "";
  private static final String PASSWORD = "";

  public VGRegionWebServiceImplPortType createWebService() {
  
  // This will set login for basic http auth used in webservice 
  Authenticator.setDefault(new Authenticator() {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(USERNAME, PASSWORD.toCharArray());
    }
  });
  
  VGRegionWebServiceImpl vgRegionWebServiceService = new VGRegionWebServiceImpl();
  VGRegionWebServiceImplPortType vgRegionWebServiceImplPort = vgRegionWebServiceService.getVGRegionWebServiceImplPort();
  // Setup username and password authentication for webservice.
  BindingProvider bindingProvider = (BindingProvider) vgRegionWebServiceImplPort;
  bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, USERNAME);
  bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);
  return vgRegionWebServiceImplPort;
}

}
