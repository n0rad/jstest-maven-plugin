package net.awired.jstest.result;

import java.util.HashMap;
import org.apache.maven.plugin.logging.Log;

public class JsTestResult extends HashMap<Integer, RunResult> {

    private static final long serialVersionUID = 1L;

    public boolean isFullyFinished() {
        if (this.size() == 0) {
            return false;
        }
        for (RunResult res : this.values()) {
            if (!res.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public String buildResult(Log log) {
        StringBuilder builder = new StringBuilder();
        boolean firstParsed = false;
        builder.append("\nResults :\n\n");

        int tests;
        int failures;
        int errors;
        int skipped;

        for (RunResult results : this.values()) {
            if (!firstParsed) {
                builder.append("JsTests run: ");
                tests = results.findTests();
                failures = results.findFailures();
                errors = results.findErrors();
                skipped = results.findSkipped();

                builder.append(tests);
                builder.append(", Failures: ");
                builder.append(failures);
                builder.append(", Errors: ");
                builder.append(errors);
                builder.append(", Skipped: ");
                builder.append(skipped);
            } else {
                // check if results do not match
            }
            builder.append(results.toString());
        }
        return builder.toString();
    }
}
