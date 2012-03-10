package net.awired.jstest.report;

import net.awired.jstest.result.RunResult;
import net.awired.jstest.result.RunResults;
import net.awired.jstest.result.SuiteResult;

public interface Report {

    void reportSuite(SuiteResult suiteResult);

    //    void reportTest();

    void reportRun(RunResult runResult);

    void reportGlobal(RunResults runResults);

}
