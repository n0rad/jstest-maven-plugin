package net.awired.jstest.mojo.inherite;

import java.io.File;
import java.util.List;
import net.awired.jstest.script.ScriptDirectory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

public abstract class JsTestConfiguration extends AbstractMojo {

    /**
     * @parameter default-value="${project.build.sourceDirectory}" expression="${sourceDir}"
     */
    private File sourceDir;

    /**
     * @parameter
     */
    private List<String> sourceIncludes = ScriptDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> sourceExcludes = ScriptDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter default-value="${project.build.testSourceDirectory}" expression="${testDir}"
     */
    private File testDir;

    /**
     * @parameter
     */
    private List<String> testIncludes = ScriptDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> testExcludes = ScriptDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter expression="${overlaydir}"
     */
    private List<File> overlayDirs;

    /**
     * @parameter
     */
    private List<String> overlayIncludes = ScriptDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> overlayExcludes = ScriptDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter default-value="0" expression="${serverPort}"
     */
    private int serverPort;

    /**
     * @parameter expression="${coverage}" default-value="false"
     */
    private boolean coverage;

    ///////////////////////////////////////////////////:

    /**
     * @parameter default-value="${project.build.directory}${file.separator}jstest"
     */
    private File jsTestTargetDir;

    /**
     * @parameter default-value="${project.build.directory}${file.separator}jstest${file.separator}coverage.dat"
     */
    private File coverageReportFile;

    /**
     * @parameter default-value="${project.build.directory}${file.separator}jstest${file.separator}src"
     */
    private File targetSourceDirectory;

    /**
     * @parameter default-value="${project.build.directory}${file.separator}jstest${file.separator}instrumented"
     */
    private File instrumentedDirectory;

    /**
     * @parameter default-value="${project.build.directory}${file.separator}jstest${file.separator}overlays"
     */
    private File overlayDirectory;

    /**
     * @parameter default-value="${project}"
     */
    private MavenProject mavenProject;

    //////////////////////////////////////////////////////////

    public ScriptDirectory getSourceScriptDirectory() {
        return new ScriptDirectory(sourceDir, sourceIncludes, sourceExcludes);
    }

    public File getOverlayDirectory() {
        return overlayDirectory;
    }

    public MavenProject getMavenProject() {
        return mavenProject;
    }

    public boolean isCoverage() {
        return coverage;
    }

    public File getInstrumentedDirectory() {
        return instrumentedDirectory;
    }

    public File getTargetSourceDirectory() {
        return targetSourceDirectory;
    }

    public File getSourceDir() {
        return sourceDir;
    }

}
