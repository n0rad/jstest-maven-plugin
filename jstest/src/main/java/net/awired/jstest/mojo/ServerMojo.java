package net.awired.jstest.mojo;

import java.util.ArrayList;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.server.JsTestHandler;
import net.awired.jstest.server.JsTestServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal server
 * @execute lifecycle="jstest-lifecycle" phase="generate-sources"
 * @requiresDirectInvocation true
 */
public class ServerMojo extends AbstractJsTestMojo {

    public static final String INSTRUCTION_FORMAT = "\n\n"
            + "Server started--it's time to spec some JavaScript! You can run your specs as you develop by visiting this URL in a web browser: \n\n"
            + "  http://localhost:%s"
            + "\n\n"
            + "The server will monitor these two directories for scripts that you add, remove, and change:\n\n"
            + "  source directory: %s\n\n"
            + "  spec directory: %s"
            + "\n\n"
            + "Just leave this process running as you test-drive your code, refreshing your browser window to re-run your specs. You can kill the server with Ctrl-C when you're done.";

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        JsTestServer jsTestServer = new JsTestServer(getLog(), getServerPort());
        try {
            ResourceResolver scriptResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(true),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            jsTestServer.startServer(new JsTestHandler(getLog(), scriptResolver, buildAmdRunnerType(),
                    buildTestType(), true));
            getLog().info(INSTRUCTION_FORMAT);
            jsTestServer.join();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start Jstest server", e);
        } finally {
            jsTestServer.close();
        }
    }
}
