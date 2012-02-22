package se.vgregion.mobile.hriv.ws;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import se.vgregion.mobile.hriv.kivws.VGRegionWebServiceImpl;
import se.vgregion.mobile.hriv.kivws.VGRegionWebServiceImplPortType;
import se.vgregion.ssl.ConvenientSslContextFactory;

public class KivwsFactoryBean {

    private final String username;
    private final String password;
    private final String truststore;
    private final String truststorePassword;

    public KivwsFactoryBean(String username, String password, String truststore, String truststorePassword) {
        this.username = username;
        this.password = password;
        this.truststore = truststore;
        this.truststorePassword = truststorePassword;
    }

    public VGRegionWebServiceImplPortType createWebService() {

        // This will set login for basic http auth used in webservice
        Authenticator.setDefault(new PasswordAuthenticator(username, password));

        // First we need to set the SslContext in order to fetch the web service definition
        ConvenientSslContextFactory sslContextFactory = new ConvenientSslContextFactory(truststore, truststorePassword,
                null, null);
        try {
            SSLContext.setDefault(sslContextFactory.createSslContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VGRegionWebServiceImpl vgRegionWebServiceService = new VGRegionWebServiceImpl();
        VGRegionWebServiceImplPortType vgRegionWebServiceImplPort = vgRegionWebServiceService
                .getVGRegionWebServiceImplPort();

        // Then we need to configure the actual web service proxy
        configureTls(vgRegionWebServiceImplPort);

        // Setup username and password authentication for webservice.
        BindingProvider bindingProvider = (BindingProvider) vgRegionWebServiceImplPort;
        bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
        bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
        return vgRegionWebServiceImplPort;
    }

    private void configureTls(VGRegionWebServiceImplPortType vgRegionWebServiceImplPort) {
        Client clientProxy = ClientProxy.getClient(vgRegionWebServiceImplPort);
        HTTPConduit httpConduit = (HTTPConduit) clientProxy.getConduit();
        try {
            httpConduit.setTlsClientParameters(setupTlsClientParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TLSClientParameters setupTlsClientParameters() throws KeyStoreException, NoSuchAlgorithmException,
            IOException, CertificateException {

        InputStream resourceAsStream = null;
        try {
            resourceAsStream = getClass().getResourceAsStream(truststore);

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(resourceAsStream, truststorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            TLSClientParameters tlsClientParameters = new TLSClientParameters();
            tlsClientParameters.setTrustManagers(trustManagerFactory.getTrustManagers());
            tlsClientParameters.setDisableCNCheck(true);

            return tlsClientParameters;
        } finally {
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
        }
    }

    private static class PasswordAuthenticator extends Authenticator {
        private String username;
        private String password;

        private PasswordAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password.toCharArray());
        }
    }

}
