package net.awired.jstest.script;

import static java.util.Arrays.asList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.plexus.util.DirectoryScanner;

public class ScriptDirectoryScanner {

    private DirectoryScanner directoryScanner = new DirectoryScanner();

    public List<String> scan(ScriptDirectory scriptDirectory) {
        Set<String> set = new LinkedHashSet<String>();
        for (String include : scriptDirectory.getIncludes()) {
            set.addAll(performScan(scriptDirectory.getDirectory(), include, scriptDirectory.getExcludes()));
        }
        return new ArrayList<String>(set);
    }

    private List<String> performScan(File directory, String include, List<String> excludes) {
        directoryScanner.setBasedir(directory);
        directoryScanner.setIncludes(new String[] { include });
        directoryScanner.setExcludes(excludes.toArray(new String[] {}));
        directoryScanner.addDefaultExcludes();
        directoryScanner.scan();
        ArrayList<String> result = new ArrayList<String>(asList(directoryScanner.getIncludedFiles()));
        Collections.sort(result);
        return result;
    }

}
