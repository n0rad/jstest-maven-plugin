package net.awired.jstest.mojo;

import java.net.URL;
import java.util.ArrayList;
import net.awired.jstest.executor.RunnerExecutor;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.result.RunResult;
import net.awired.jstest.server.JsTestServer;
import net.awired.jstest.server.handler.JsTestHandler;
import net.awired.jstest.server.handler.ResultHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @component
 * @goal test
 * @phase test
 * @execute lifecycle="jstest-lifecycle" phase="process-test-resources"
 */
public class TestMojo extends AbstractJsTestMojo {

    private static final String ERROR_MSG = "There are test failures.\n\nPlease refer to %s for the individual test results.";

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        if (isSkipTests()) {
            getLog().info("Skipping JsTest");
            return;
        }

        JsTestServer jsTestServer = new JsTestServer(getLog(), getTestPort(), isTestPortFindFree());
        RunnerExecutor executor = null;
        try {
            ResourceResolver resourceResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(false),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            ResultHandler resultHandler = new ResultHandler(getLog(), getPreparedReportDir());
            jsTestServer.startServer(new JsTestHandler(resultHandler, getLog(), resourceResolver, buildAmdRunnerType(),
                    buildTestType(), false, getLog().isDebugEnabled()));

            if (isEmulator()) {
                executor = new RunnerExecutor();
                executor.execute(new URL("http://localhost:" + getDevPort() + "/?emulator=true"));
            }

            // let browsers detect that server is back
            Thread.sleep(1000);

            if (!resultHandler.waitAllResult(10000, 1000)) {
                throw new MojoFailureException("Do not receive all test results from clients");
            }

            RunResult buildAggregatedResult = resultHandler.getRunResults().buildAggregatedResult();
            if (buildAggregatedResult.findErrors() > 0 || buildAggregatedResult.findFailures() > 0) {
                String message = String.format(ERROR_MSG, getPreparedReportDir());
                if (isIgnoreFailure()) {
                    getLog().error(message);
                } else {
                    throw new MojoFailureException(message);
                }
            }
        } catch (MojoFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("JsTest execution failure", e);
        } finally {
            if (executor != null) {
                executor.close();
            }
            jsTestServer.close();
        }
    }
}
