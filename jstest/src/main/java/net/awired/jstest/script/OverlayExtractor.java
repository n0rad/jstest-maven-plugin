package net.awired.jstest.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.war.Overlay;
import org.apache.maven.plugin.war.overlay.OverlayManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

public class OverlayExtractor {

    private final Log log;
    private ArchiverManager archiverManager;

    public OverlayExtractor(Log log, ArchiverManager archiverManager) {
        this.log = log;
        this.archiverManager = archiverManager;
    }

    public void extract(File rootDirectory, MavenProject mavenProject) {
        try {
            List<Overlay> overlays = new ArrayList<Overlay>();
            final Overlay currentProjectOverlay = Overlay.createInstance();
            final OverlayManager overlayManager = new OverlayManager(overlays, mavenProject, "**/**", "META-INF/**",
                    currentProjectOverlay);
            final List<Overlay> resolvedOverlays = overlayManager.getOverlays();
            for (Overlay overlay2 : resolvedOverlays) {
                if (!overlay2.isCurrentProject()) {
                    unpackOverlay(rootDirectory, overlay2);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot process overlay projects", e);
        }
    }

    private void unpackOverlay(File rootDirectory, Overlay overlay) throws MojoExecutionException {
        File overlayOutput = generateOverlayOutput(rootDirectory, overlay);
        overlayOutput.mkdirs();
        // TODO: not sure it's good, we should reuse the markers of the dependency plugin
        if (FileUtils.sizeOfDirectory(overlayOutput) == 0
                || overlay.getArtifact().getFile().lastModified() > overlayOutput.lastModified()) {
            doUnpack(overlay.getArtifact().getFile(), overlayOutput);
        } else {
            log.debug("Overlay [" + overlay + "] was already unpacked");
        }
    }

    private File generateOverlayOutput(File rootDirectory, Overlay overlay) {
        String subdir = overlay.getGroupId() + File.separator + overlay.getArtifactId();
        if (overlay.getClassifier() != null) {
            subdir += "-" + overlay.getClassifier();
        }
        return new File(rootDirectory, subdir);
    }

    private void doUnpack(File file, File unpackDirectory) throws MojoExecutionException {
        String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();

        try {
            UnArchiver unArchiver = archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            unArchiver.setDestDirectory(unpackDirectory);
            unArchiver.setOverwrite(true);
            unArchiver.extract();
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Error unpacking file [" + file.getAbsolutePath() + "]" + "to ["
                    + unpackDirectory.getAbsolutePath() + "]", e);
        } catch (NoSuchArchiverException e) {
            log.warn("Skip unpacking dependency file [" + file.getAbsolutePath() + " with unknown extension ["
                    + archiveExt + "]");
        }
    }

}
