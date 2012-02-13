package net.awired.jstest.mojo.inherite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.awired.jstest.common.StringStacktrace;
import net.awired.jstest.resource.ResourceDirectory;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

public abstract class AbstractJsTestMojo extends JsTestConfiguration {

    protected abstract void run() throws MojoExecutionException, MojoFailureException;

    private StringStacktrace stringStacktrace = new StringStacktrace();

    /**
     * @component role="org.codehaus.plexus.archiver.manager.ArchiverManager"
     * @required
     */
    protected ArchiverManager archiverManager;

    public final void execute() throws MojoExecutionException, MojoFailureException {
        try {
            run();
        } catch (MojoFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException("The jstest-maven-plugin encountered an exception: \n"
                    + stringStacktrace.stringify(e), e);
        }
    }

    public List<ResourceDirectory> buildOverlaysResourceDirectories() {
        List<ResourceDirectory> overlays = new ArrayList<ResourceDirectory>();
        File overlayDirectory = getOverlayDirectory();
        File[] groupIdDirs = overlayDirectory.listFiles();
        if (groupIdDirs != null) {
            for (File groupIdDir : groupIdDirs) {
                File[] artifactIdDirs = groupIdDir.listFiles();
                for (File resource : artifactIdDirs) {
                    overlays.add(new ResourceDirectory(resource, ResourceDirectory.DEFAULT_INCLUDES,
                            ResourceDirectory.DEFAULT_EXCLUDES));
                }
            }
        }
        return overlays;
    }
}
