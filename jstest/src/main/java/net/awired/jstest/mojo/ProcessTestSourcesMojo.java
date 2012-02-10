package net.awired.jstest.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import net.awired.jscoverage.instrumentation.JsInstrumentedSource;
import net.awired.jscoverage.instrumentation.JsInstrumentor;
import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import net.awired.jstest.script.DirectoryCopier;
import net.awired.jstest.script.FileUtilsWrapper;
import net.awired.jstest.script.ScriptDirectory;
import net.awired.jstest.script.ScriptDirectoryScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal processTestSources
 * @phase process-test-sources
 */
public class ProcessTestSourcesMojo extends AbstractJsTestMojo {

    private ScriptDirectoryScanner scriptDirScanner = new ScriptDirectoryScanner();
    private JsInstrumentor jsInstrumentor = new JsInstrumentor();
    private FileUtilsWrapper fileUtilsWrapper = new FileUtilsWrapper();
    private DirectoryCopier directoryCopier = new DirectoryCopier();

    @Override
    protected void run() throws MojoExecutionException, MojoFailureException {
        try {
            directoryCopier.copyDirectory(getSourceDir(), getTargetSourceDirectory());
        } catch (IOException e) {
            throw new RuntimeException("Cannot copy source directory to target", e);
        }
        if (isCoverage()) {
            getLog().info("Instrumentation of javascript sources");
            processInstrumentSources();
        }
    }

    private void processInstrumentSources() {
        if (isCoverage()) {
            ScriptDirectory sourceScriptDirectory = getSourceScriptDirectory();
            List<String> scan = scriptDirScanner.scan(sourceScriptDirectory);
            for (String file : scan) {
                try {
                    JsInstrumentedSource instrument = jsInstrumentor.instrument(file,
                            fileUtilsWrapper.readFileToString(new File(sourceScriptDirectory.getDirectory(), file)));
                    File instrumentedfile = new File(getInstrumentedDirectory(), file);
                    fileUtilsWrapper.forceMkdir(instrumentedfile.getParentFile());
                    fileUtilsWrapper.writeStringToFile(instrumentedfile, instrument.getIntrumentedSource(), "UTF-8");
                } catch (FileNotFoundException e) {
                    throw new IllegalStateException("cannot find source code to instrument", e);
                } catch (Exception e) {
                    throw new IllegalStateException("cannot instrument source code", e);
                }
            }
        }
    }

}
