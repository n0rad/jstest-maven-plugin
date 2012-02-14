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
 * @component
 * @goal test
 * @phase test
 * @execute lifecycle="jstest-lifecycle" phase="process-test-resources"
 */
public class TestMojo extends AbstractJsTestMojo {

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        JsTestServer jsTestServer = new JsTestServer(getLog(), getServerPort());
        try {
            ResourceResolver scriptResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(false),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            jsTestServer.startServer(new JsTestHandler(getLog(), scriptResolver, buildAmdRunnerType(),
                    buildTestType()));
            getLog().info("Running test server");
            jsTestServer.join();
        } catch (Exception e) {
            throw new RuntimeException("Cannot start Jstest server", e);
        } finally {
            jsTestServer.close();
        }
    }

}
