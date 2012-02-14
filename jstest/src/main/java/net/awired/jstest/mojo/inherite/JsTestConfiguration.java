package net.awired.jstest.mojo.inherite;

import java.io.File;
import java.util.List;
import net.awired.jstest.resource.ResourceDirectory;
import net.awired.jstest.runner.AmdRunnerType;
import net.awired.jstest.runner.TestType;
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
    private List<String> sourceIncludes = ResourceDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> sourceExcludes = ResourceDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter default-value="${project.build.testSourceDirectory}" expression="${testDir}"
     */
    private File testDir;

    /**
     * @parameter
     */
    private List<String> testIncludes = ResourceDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> testExcludes = ResourceDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter expression="${overlaydirs}"
     */
    private List<File> preloadOverlayDirs;

    /**
     * @parameter
     */
    private List<String> overlayIncludes = ResourceDirectory.DEFAULT_INCLUDES;

    /**
     * @parameter
     */
    private List<String> overlayExcludes = ResourceDirectory.DEFAULT_EXCLUDES;

    /**
     * @parameter default-value="0" expression="${serverPort}"
     */
    private int serverPort;

    /**
     * @parameter expression="${coverage}" default-value="false"
     */
    private boolean coverage;

    /**
     * @parameter expression="${amdType}"
     */
    private AmdRunnerType runnerType = AmdRunnerType.NONE;

    /**
     * @parameter expression="${testType}"
     */
    private TestType testType = TestType.JASMINE;

    /**
     * @parameter expression="${runnerTemplate}"
     */
    private String runnerTemplate;

    /**
     * @parameter expression="${runnerAmdFile}"
     */
    private String runnerAmdFile;

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
    private File targetSrcDir;

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

    public AmdRunnerType buildAmdRunnerType() {
        if (runnerAmdFile != null) {
            this.runnerType.setAmdFile(runnerAmdFile);
        }
        if (runnerTemplate != null) {
            this.runnerType.setTemplate(runnerTemplate);
        }
        return this.runnerType;
    }

    public TestType buildTestType() {
        return testType;
    }

    private ResourceDirectory buildInstrumentedSrcResourceDirectory() {
        return new ResourceDirectory(targetSrcDir, sourceIncludes, sourceExcludes);
    }

    private ResourceDirectory buildTargetSrcResourceDirectory() {
        return new ResourceDirectory(targetSrcDir, sourceIncludes, sourceExcludes);
    }

    public ResourceDirectory buildSrcResourceDirectory() {
        ResourceDirectory resourceDirectory = new ResourceDirectory(sourceDir, sourceIncludes, sourceExcludes);
        resourceDirectory.setUpdatable(true);
        return resourceDirectory;
    }

    public ResourceDirectory buildCurrentSrcDir(boolean serverMode) {
        if (serverMode) {
            return buildSrcResourceDirectory();
        } else if (isCoverage()) {
            return buildInstrumentedSrcResourceDirectory();
        } else {
            return buildTargetSrcResourceDirectory();
        }
    }

    public ResourceDirectory buildTestResourceDirectory() {
        ResourceDirectory resourceDirectory = new ResourceDirectory(testDir, testIncludes, testExcludes);
        resourceDirectory.setUpdatable(true);
        return resourceDirectory;
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
        return targetSrcDir;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public int getServerPort() {
        return serverPort;
    }

}
