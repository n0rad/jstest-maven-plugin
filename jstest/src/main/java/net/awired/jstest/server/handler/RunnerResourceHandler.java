package net.awired.jstest.server.handler;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.server.Request;
import com.google.common.io.ByteStreams;

public class RunnerResourceHandler {

    public static final String RUNNER_RESOURCE_PATH = "/runnerResource/";
    private final Log log;

    public RunnerResourceHandler(Log log) {
        this.log = log;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("Serve runner resource : " + target);
        String contentType = "text/html";
        if (target.endsWith(".js")) {
            contentType = "application/javascript";
        } else if (target.endsWith(".css")) {
            contentType = "text/css";
        }
        response.setContentType(contentType + ";charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        InputStream resourceAsStream = getClass().getResourceAsStream(target);
        if (resourceAsStream == null) {
            resourceAsStream = getClass().getResourceAsStream(target.substring(RUNNER_RESOURCE_PATH.length() - 1));
        }
        ByteStreams.copy(resourceAsStream, response.getOutputStream());

    }

}
