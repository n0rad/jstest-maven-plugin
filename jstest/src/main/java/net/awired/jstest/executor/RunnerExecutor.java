package net.awired.jstest.executor;

import java.lang.reflect.Constructor;
import java.net.URL;
import org.apache.maven.plugin.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

public class RunnerExecutor {

    private final WebDriver driver;

    public RunnerExecutor() {
        this.driver = createDriver();
        if (!(driver instanceof JavascriptExecutor)) {
            throw new RuntimeException("The provided web driver can't execute JavaScript: " + driver.getClass());
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

    public void execute(URL runnerUrl, int timeout, boolean debug, Log log) {
        try {
            driver.get(runnerUrl.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        driver.quit();
    }

}
