package net.awired.jstest.mojo;

import java.net.URL;
import java.util.ArrayList;
import net.awired.jstest.executor.RunnerExecutor;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.server.JsTestHandler;
import net.awired.jstest.server.JsTestServer;
import net.awired.jstest.server.ResultHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @component
 * @goal test
 * @phase test
 * @execute lifecycle="jstest-lifecycle" phase="process-test-resources"
 */
public class TestMojo extends AbstractJsTestMojo {

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        if (isSkipTests()) {
            getLog().info("Skipping JsTest");
            return;
        }

        JsTestServer jsTestServer = new JsTestServer(getLog(), getServerPort());
        RunnerExecutor executor = null;
        try {
            ResourceResolver scriptResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(false),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            ResultHandler resultHandler = new ResultHandler(getLog());
            jsTestServer.startServer(new JsTestHandler(resultHandler, getLog(), scriptResolver, buildAmdRunnerType(),
                    buildTestType(), false, getLog().isDebugEnabled()));

            executor = new RunnerExecutor();
            executor.execute(new URL("http://localhost:" + getServerPort() + "/?emulator=true"), 300, true, getLog());

            // let browser detect that server is back
            Thread.sleep(1000);

            if (!resultHandler.waitAllResult(10000, 1000)) {
                throw new MojoFailureException("Do not receive all test results from clients");
            } else {
                System.out.println(resultHandler.getRunResults().toString());
            }
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
