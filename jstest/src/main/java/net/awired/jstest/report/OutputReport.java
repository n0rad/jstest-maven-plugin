package net.awired.jstest.report;

import java.io.PrintStream;
import java.text.DecimalFormat;
import net.awired.jstest.result.RunResult;
import net.awired.jstest.result.RunResults;
import net.awired.jstest.result.SuiteResult;
import org.apache.maven.plugin.logging.Log;
import com.google.common.base.Preconditions;

public class OutputReport implements Report {

    private static final String HEADER = "\n" //
            + "-------------------------------------------------------\n" //
            + " J S   T E S T S\n" //
            + "-------------------------------------------------------";

    private boolean firstReceived;
    private PrintStream out = System.out;
    private final Log log;
    private DecimalFormat coverageFormat = new DecimalFormat("#.#");

    public OutputReport(Log log) {
        this.log = log;
    }

    @Override
    public void reportSuite(SuiteResult suiteResult) {
        Preconditions.checkNotNull(suiteResult.getRunResult(), "RunResult must be set in SuiteResult");
        checkPrintHeader();

        StringBuilder builder = new StringBuilder();
        builder.append("Run suite: ");
        builder.append(suiteResult.getName());
        builder.append(", Agent: ");
        builder.append(suiteResult.getRunResult().userAgentToString());
        builder.append("\nTests run: ");
        builder.append(suiteResult.getTests().size());
        builder.append(", Failures: ");
        builder.append(suiteResult.getFailures());
        builder.append(", Errors: ");
        builder.append(suiteResult.getErrors());
        builder.append(", Skipped: ");
        builder.append(suiteResult.getSkipped());
        builder.append(", Time elapsed: ");
        builder.append(suiteResult.getDuration());
        builder.append(" ms");
        out.println(builder);
    }

    private void checkPrintHeader() {
        if (!firstReceived) {
            out.println(HEADER);
            firstReceived = true;
        }
    }

    @Override
    public void reportRun(RunResult runresult) {
    }

    @Override
    public void reportGlobal(RunResults runResults) {
        StringBuilder builder = new StringBuilder();
        boolean firstParsed = false;
        builder.append("\nResults :\n\n");

        for (RunResult result : runResults.values()) {
            if (!firstParsed) {
                builder.append("Run: ");
                builder.append(result.findTests());
                builder.append(", Failures: ");
                builder.append(result.findFailures());
                builder.append(", Errors: ");
                builder.append(result.findErrors());
                builder.append(", Skipped: ");
                builder.append(result.findSkipped());
                builder.append(", Time elapsed: ");
                builder.append(result.getDuration());
                builder.append("ms");
                if (result.getCoverageResult() != null) {
                    builder.append(", Coverage: ");
                    builder.append(coverageFormat.format(result.getCoverageResult().findCoveragePercent()));
                    builder.append('%');
                }
                builder.append(", Agent: ");
                builder.append(result.userAgentToString());
                builder.append("\n");
            }
        }
        out.println(builder);
    }

}
