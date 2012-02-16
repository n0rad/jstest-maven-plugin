package net.awired.jstest.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.resource.ResourceResolver;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.server.Request;
import com.google.common.io.ByteStreams;

public class ResourceHandler {

    private ResourceResolver resourceResolver;
    private final Log log;

    public ResourceHandler(Log log, ResourceResolver resourceResolver) {
        this.log = log;
        this.resourceResolver = resourceResolver;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        File file = resourceResolver.getResource(target);
        if (file != null) {
            log.debug("Serve resource : " + target + " to target file :" + file);
            String contentType = "text/html";
            if (target.endsWith(".js")) {
                contentType = "application/javascript";
            } else if (target.endsWith(".css")) {
                contentType = "text/css";
            } else if (target.endsWith(".png")) {
                contentType = " image/png";
            } else if (target.endsWith(".gif")) {
                contentType = " image/gif";
            }
            response.setContentType(contentType + ";charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            ByteStreams.copy(new FileInputStream(file), response.getOutputStream());
        }
    }

}
