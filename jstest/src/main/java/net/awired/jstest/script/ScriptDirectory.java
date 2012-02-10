package net.awired.jstest.script;

import static java.util.Arrays.asList;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class ScriptDirectory {

    public static final List<String> DEFAULT_INCLUDES = asList("**" + File.separator + "**");
    public static final List<String> DEFAULT_EXCLUDES = Collections.emptyList();

    private File directory;
    private List<String> includes;
    private List<String> excludes;
    private boolean updatable;
    private int priority;

    public ScriptDirectory() {
    }

    public ScriptDirectory(File directory, List<String> sourceIncludes, List<String> sourceExcludes) {
        this.directory = directory;
        this.includes = sourceIncludes;
        this.excludes = sourceExcludes;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

}
