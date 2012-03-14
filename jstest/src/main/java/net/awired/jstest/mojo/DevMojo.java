package net.awired.jstest.mojo;

import java.util.ArrayList;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.server.JsTestServer;
import net.awired.jstest.server.handler.JsTestHandler;
import net.awired.jstest.server.handler.ResultHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal dev
 * @execute lifecycle="jstest-lifecycle" phase="generate-test-sources"
 * @requiresDirectInvocation true
 */
public class DevMojo extends AbstractJsTestMojo {

    public static final String INSTRUCTION_FORMAT = "\n\n"
            + "You can run your tests as you develop by visiting this URL in a web browser: \n\n"
            + "  http://localhost:%s"
            + "\n\n"
            + "The server will monitor these two directories for scripts that you add, remove, and change:\n\n"
            + "  source directory: %s\n\n"
            + "  test directory: %s"
            + "\n\n"
            + "Leave this process running as you test-drive your code, refreshing your browser window to re-run tests.\n"
            + "You can kill the server with Ctrl-C when you're done.";

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        JsTestServer jsTestServer = new JsTestServer(getLog(), getDevPort(), false);
        try {
            ResourceResolver scriptResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(true),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            //TODO remove resultHandler creation we dont need it here
            ResultHandler resultHandler = new ResultHandler(getLog(), null);
            jsTestServer.startServer(new JsTestHandler(resultHandler, getLog(), scriptResolver, buildAmdRunnerType(),
                    buildTestType(), true, getLog().isDebugEnabled()));
            getLog().info(String.format(INSTRUCTION_FORMAT, getDevPort(), getSourceDir(), getTestDir()));
            jsTestServer.join();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start Jstest server", e);
        } finally {
            jsTestServer.close();
        }
    }
}
