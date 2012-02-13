package net.awired.jstest.resource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.logging.Log;

public class ResourceResolver {

    public static final String SRC_RESOURCE_PREFIX = "/src/";
    public static final String TEST_RESOURCE_PREFIX = "/test/";
    private ResourceDirectoryScanner directoryScanner = new ResourceDirectoryScanner();

    private final Log log;
    private final ResourceDirectory source;
    private final ResourceDirectory test;
    private final List<ResourceDirectory> overlays;
    private final List<ResourceDirectory> preloadOverlayDirs;

    private final Map<String, File> resources = new HashMap<String, File>();

    public ResourceResolver(Log log, ResourceDirectory source, ResourceDirectory test,
            List<ResourceDirectory> overlays, List<ResourceDirectory> preloadOverlayDirs) {
        this.log = log;
        this.source = source;
        this.test = test;
        this.overlays = overlays;
        this.preloadOverlayDirs = preloadOverlayDirs;

        registerResourcesToMap(SRC_RESOURCE_PREFIX, directoryScanner.scan(source), source.getDirectory(), true);
        registerResourcesToMap(TEST_RESOURCE_PREFIX, directoryScanner.scan(test), test.getDirectory(), true);
        for (ResourceDirectory overlay : overlays) {
            registerResourcesToMap(SRC_RESOURCE_PREFIX, directoryScanner.scan(overlay), overlay.getDirectory(), true);
        }
        for (ResourceDirectory overlayPreload : preloadOverlayDirs) {
            registerResourcesToMap(SRC_RESOURCE_PREFIX, directoryScanner.scan(overlayPreload),
                    overlayPreload.getDirectory(), false);
        }

        if (log.isDebugEnabled()) {
            log.debug("Resources resolved by the server : ");
            for (String resourcePath : resources.keySet()) {
                log.debug("* " + resourcePath + " to " + resources.get(resourcePath));
            }
        }
    }

    private void registerResourcesToMap(String prefix, List<String> founds, File path, boolean logOnConflict) {
        for (String found : founds) {
            File fullPath = new File(path, found);
            File alreadyRegistered = resources.get(found);
            if (alreadyRegistered != null) {
                log.warn("Resource conflics for : " + found + ". Found in " + alreadyRegistered + " and in "
                        + fullPath);
            } else {
                resources.put(prefix + found, fullPath);
            }
        }
    }

    public File getScript(String path) {
        return resources.get(path);
    }

    public void updateChangeableDirectories() {
        if (source.isUpdatable()) {
            log.debug("Updating directory files for " + source.getDirectory());
            registerResourcesToMap(SRC_RESOURCE_PREFIX, directoryScanner.scan(source), source.getDirectory(), false);
        }
        if (test.isUpdatable()) {
            log.debug("Updating directory files for " + test.getDirectory());
            registerResourcesToMap(TEST_RESOURCE_PREFIX, directoryScanner.scan(test), test.getDirectory(), false);
        }
        for (ResourceDirectory overlayPreload : preloadOverlayDirs) {
            log.debug("Updating directory files for " + overlayPreload.getDirectory());
            registerResourcesToMap(SRC_RESOURCE_PREFIX, directoryScanner.scan(overlayPreload),
                    overlayPreload.getDirectory(), false);
        }
    }
}
