package net.awired.jstest.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.runner.AmdRunnerType;
import net.awired.jstest.runner.RunnerGenerator;
import net.awired.jstest.runner.TestType;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JsTestHandler extends AbstractHandler {

    private final Log log;
    private final ResourceResolver resourceResolver;
    private final FaviconHandler faviconHandler = new FaviconHandler();
    private final ResourceHandler resourceHandler;
    private final RunnerResourceHandler runnerHandler;
    private final RunnerGenerator runnerGenerator = new RunnerGenerator();
    private final AmdRunnerType runnerType;
    private final TestType testType;

    public JsTestHandler(Log log, ResourceResolver resolver, AmdRunnerType runnerType, TestType testType) {
        this.log = log;
        this.resourceResolver = resolver;
        this.runnerType = runnerType;
        this.testType = testType;
        this.runnerHandler = new RunnerResourceHandler(log);
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
        } else if (target.startsWith(RunnerResourceHandler.RUNNER_RESOURCE_PATH)) {
            runnerHandler.handle(target, baseRequest, request, response);
        } else if (target.equals("/")) {
            resourceResolver.updateChangeableDirectories();

            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().write(runnerGenerator.generate(runnerType, testType, resourceResolver));
            // give root page
        } else if (target.equals("/result")) {
            // parse result
        }
    }

}
