package net.awired.jstest.mojo;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import net.awired.jstest.executor.RunnerExecutor;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.result.JasmineResult;
import net.awired.jstest.server.JsTestHandler;
import net.awired.jstest.server.JsTestServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @component
 * @goal test
 * @phase test
 * @execute lifecycle="jstest-lifecycle" phase="process-test-resources"
 */
public class TestMojo extends AbstractJsTestMojo {

    private String HEADER = "\n" //
            + "-------------------------------------------------------\n" //
            + " J S   T E S T S\n" //
            + "-------------------------------------------------------";

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        if (isSkipTests()) {
            getLog().info("Skipping JsTest");
            return;
        }
        JsTestServer jsTestServer = new JsTestServer(getLog(), getServerPort());
        try {
            ResourceResolver scriptResolver = new ResourceResolver(getLog(), buildCurrentSrcDir(false),
                    buildTestResourceDirectory(), buildOverlaysResourceDirectories(),
                    new ArrayList<ResourceDirectory>());
            jsTestServer.startServer(new JsTestHandler(getLog(), scriptResolver, buildAmdRunnerType(),
                    buildTestType(), false));
            getLog().info("Running test server");
            RunnerExecutor executor = new RunnerExecutor();
            WebDriver driver = createDriver();
            getLog().info(HEADER);
            JasmineResult result = executor.execute(new URL("http://localhost:" + getServerPort()),
                    getResultReportFile(), driver, 300, true, getLog(), "documentation", isCoverage(),
                    getCoverageReportFile());
            getLog().info(result.getDetails());
        } catch (Exception e) {
            throw new RuntimeException("Cannot start Jstest server", e);
        } finally {
            jsTestServer.close();
        }
    }

    private WebDriver createDriver() {
        if (!HtmlUnitDriver.class.getName().equals("org.openqa.selenium.htmlunit.HtmlUnitDriver")) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends WebDriver> klass = (Class<? extends WebDriver>) Class
                        .forName("org.openqa.selenium.htmlunit.HtmlUnitDriver");
                Constructor<? extends WebDriver> ctor = klass.getConstructor();
                return ctor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Couldn't instantiate webDriverClassName", e);
            }
        }

        // We have extra configuration to do to the HtmlUnitDriver
        BrowserVersion htmlUnitBrowserVersion;
        try {
            htmlUnitBrowserVersion = (BrowserVersion) BrowserVersion.class.getField("FIREFOX_3").get(
                    BrowserVersion.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HtmlUnitDriver driver = new HtmlUnitDriver(htmlUnitBrowserVersion) {
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                client.setAjaxController(new NicelyResynchronizingAjaxController());

                //Disables stuff like this "com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl notify WARNING: Obsolete content type encountered: 'text/javascript'."
                if (!false) {
                    client.setIncorrectnessListener(new IncorrectnessListener() {
                        public void notify(String arg0, Object arg1) {
                        }
                    });
                }

                return client;
            };
        };
        driver.setJavascriptEnabled(true);
        return driver;
    }

}
