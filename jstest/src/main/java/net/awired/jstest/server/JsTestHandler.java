package net.awired.jstest.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.runner.Runner;
import net.awired.jstest.runner.RunnerType;
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
    private final ResultHandler resultHandler;
    private final Runner runnerGenerator;
    private final RunnerType runnerType;
    private final TestType testType;

    private int browserId = 0;

    public JsTestHandler(ResultHandler result, Log log, ResourceResolver resolver, RunnerType runnerType,
            TestType testType, boolean serverMode, boolean debug) {
        this.log = log;
        this.resourceResolver = resolver;
        this.runnerType = runnerType;
        this.testType = testType;
        this.runnerHandler = new RunnerResourceHandler(log);
        this.resourceHandler = new ResourceHandler(log, resolver);
        this.resultHandler = result;
        this.runnerGenerator = runnerType.buildRunner(testType, resolver, serverMode, debug);
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            response.addDateHeader("EXPIRES", 0L);
            response.addHeader("CACHE_CONTROL", "NO_CACHE");
            response.addHeader("PARAGMA", "NO CACHE");
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
                response.getWriter().write(runnerGenerator.generate(generateBrowserId(), isEmulator(request)));
                // give root page
            } else if (target.startsWith("/result/")) {
                resultHandler.handle(target, baseRequest, request, response);
            }
        } catch (Exception e) {
            log.error("Error on processing request in test server", e);
        }

    }

    private boolean isEmulator(HttpServletRequest request) {
        boolean emulator = false;
        String emulatorParam = request.getParameter("emulator");
        if (emulatorParam != null) {
            emulator = Boolean.valueOf(emulatorParam);
        }
        return emulator;
    }

    public synchronized int generateBrowserId() {
        return browserId++;
    }

}
