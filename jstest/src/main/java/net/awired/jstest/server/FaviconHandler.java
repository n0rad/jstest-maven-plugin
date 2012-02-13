package net.awired.jstest.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import com.google.common.io.ByteStreams;

public class FaviconHandler {

    private static final String FAVICON = "/favicon.ico";
    private static final String FAVICON_SUCCESS = "/favicon-success.ico";
    private static final String FAVICON_ERROR = "/favicon-error.ico";
    private static final String FAVICON_FAIL = "/favicon-fail.ico";
    private static final String[] FAVICONS = new String[] { FAVICON, FAVICON_ERROR, FAVICON_FAIL, FAVICON_SUCCESS };

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        for (String favicons : FAVICONS) {
            if (favicons.equals(target)) {
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                ByteStreams.copy(getClass().getResourceAsStream(favicons), response.getOutputStream());
            }
        }
    }
}
