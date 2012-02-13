package net.awired.jstest.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.resource.ResourceResolver;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JsTestHandler extends AbstractHandler {

    private final Log log;
    private final ResourceResolver resourceResolver;
    private final FaviconHandler faviconHandler = new FaviconHandler();
    private final ResourceHandler resourceHandler;

    public JsTestHandler(Log log, ResourceResolver resolver) {
        this.log = log;
        this.resourceResolver = resolver;
        this.resourceHandler = new ResourceHandler(log, resolver);
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.addDateHeader("EXPIRES", 0L);
        if (target.startsWith("/favicon")) {
            faviconHandler.handle(target, baseRequest, request, response);
            return;
        } else if (target.startsWith(ResourceResolver.SRC_RESOURCE_PREFIX)
                || target.startsWith(ResourceResolver.TEST_RESOURCE_PREFIX)) {
            resourceHandler.handle(target, baseRequest, request, response);
        } else {
            resourceResolver.updateChangeableDirectories();
        }
    }

}
