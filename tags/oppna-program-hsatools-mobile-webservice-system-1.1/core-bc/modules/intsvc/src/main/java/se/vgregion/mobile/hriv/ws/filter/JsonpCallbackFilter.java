package se.vgregion.mobile.hriv.ws.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * A {@link Filter} which examines the request and looks for a "callback" parameter. If found the response is wrapped
 * in a javascript function call to produce a JSONP response.
 *
 * @author Patrik Bergström
 */
public class JsonpCallbackFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonpCallbackFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        @SuppressWarnings("unchecked")
        Map<String, String[]> parms = httpRequest.getParameterMap();

        if (parms.containsKey("callback")) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");
            }

            // If the callback parameter e.g. is "handleResponse" we first write "handleResponse(" to the response
            // outputStream.
            OutputStream out = httpResponse.getOutputStream();
            out.write((parms.get("callback")[0] + "(").getBytes());

            // This will append the normal response
            filterChain.doFilter(httpRequest, httpResponse);

            // Now we wrap it up by appending ");"
            out.write((");").getBytes());

            httpResponse.setContentType("text/javascript;charset=UTF-8");
            out.close();
        } else {
            filterChain.doFilter(httpRequest, httpResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
