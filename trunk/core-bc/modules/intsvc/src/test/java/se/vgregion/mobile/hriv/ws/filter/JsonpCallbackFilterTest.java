package se.vgregion.mobile.hriv.ws.filter;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockFilterChain;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Bergström
 */
public class JsonpCallbackFilterTest {
    
    @Test
    public void testDoFilter() throws Exception {

        final String jsonResponse = "{\"someKey\":\"someValue\"}";
        String someCallbackFunction = "someCallbackFunction";

        // Given
        ServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) {
                try {
                    response.getOutputStream().write(jsonResponse.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // We make a ByteArrayOutputStream which we set to an OutputStream so we can verify at the end.
        ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
        DelegatingServletOutputStream delegatingServletOutputStream = new DelegatingServletOutputStream(targetStream);

        // Populate the request parameter map with a callback function
        HashMap parameterMap = new HashMap();
        parameterMap.put("callback", new String[]{someCallbackFunction});
        when(request.getParameterMap()).thenReturn(parameterMap);
        when(response.getOutputStream()).thenReturn(delegatingServletOutputStream);
        
        JsonpCallbackFilter filter = new JsonpCallbackFilter();

        // When (the doFilter will populate the ServletOutputStream which in turn just delegates to the ByteArrayOutputStream
        filter.doFilter(request, response, filterChain);

        // Then (it should equal "someCallbackFunction({"someKey":"someValue"});"
        assertEquals(someCallbackFunction + "(" + jsonResponse + ");", targetStream.toString());
        
    }
}
