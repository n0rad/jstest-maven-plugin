package net.awired.jstest.report;

import java.io.File;
import net.awired.jstest.result.RunResult;
import net.awired.jstest.result.RunResults;
import net.awired.jstest.result.SuiteResult;
import org.apache.maven.plugin.logging.Log;

public class MultiReport implements Report {

    private OutputReport outputReport;
    private XmlReport xmlReport;

    public MultiReport(Log log, File reportDir) {
        outputReport = new OutputReport(log);
        xmlReport = new XmlReport(reportDir);
    }

    @Override
    public void reportSuite(SuiteResult suiteResult) {
        outputReport.reportSuite(suiteResult);
        xmlReport.reportSuite(suiteResult);
    }

    @Override
    public void reportRun(RunResult runResult) {
        outputReport.reportRun(runResult);
        xmlReport.reportRun(runResult);
    }

    @Override
    public void reportGlobal(RunResults runResults) {
        outputReport.reportGlobal(runResults);
        xmlReport.reportGlobal(runResults);
    }

}
