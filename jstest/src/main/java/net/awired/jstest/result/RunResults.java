package net.awired.jstest.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunResults extends HashMap<Integer, RunResult> {

    private static final long serialVersionUID = 1L;

    private RunResult aggregatedResult;

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

    @Override
    public RunResult remove(Object key) {
        aggregatedResult = null;
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends RunResult> m) {
        aggregatedResult = null;
        super.putAll(m);
    }

    @Override
    public RunResult put(Integer key, RunResult value) {
        aggregatedResult = null;
        return super.put(key, value);
    }

    public RunResult buildAggregatedResult() {
        if (aggregatedResult == null) {
            aggregatedResult = generateAggregatedResult();
        }
        return aggregatedResult;
    }

    private RunResult generateAggregatedResult() {
        RunResult res = new RunResult(-1);
        for (RunResult runResult : this.values()) {
            if (res.getCoverageResult() == null && runResult.getCoverageResult() != null) {
                res.setCoverageResult(runResult.getCoverageResult());
            }

            if (res.getDuration() < runResult.getDuration()) {
                res.setDuration(runResult.getDuration());
            }

            for (SuiteResult suiteResult : runResult.getSuiteResults()) {
                if (!res.getSuiteResults().contains(suiteResult)) {
                    SuiteResult buildAggregatedSuite = buildAggregatedSuite(this, runResult, suiteResult);
                    res.addSuite(buildAggregatedSuite);
                }
            }
        }
        return res;
    }

    public static SuiteResult buildAggregatedSuite(RunResults runResults, RunResult currentRunResult,
            SuiteResult currentSuiteResult) {
        SuiteResult res = new SuiteResult();
        List<TestResult> tests = res.getTests();
        res.setName(currentSuiteResult.getName());
        for (RunResult runResult : runResults.values()) {
            SuiteResult suiteFound = null;
            for (SuiteResult suiteResult : runResult.getSuiteResults()) {
                if (currentSuiteResult.equals(suiteResult)) {
                    suiteFound = suiteResult;
                    break;
                }
            }

            if (suiteFound.getDuration() > res.getDuration()) {
                res.setDuration(suiteFound.getDuration());
            }

            for (TestResult testResult : suiteFound.getTests()) {
                if (!tests.contains(testResult) && !testResult.isSuccess()) {
                    res.addTest(testResult);
                }
            }

        }

        for (RunResult runResult : runResults.values()) {
            SuiteResult suiteFound = null;
            for (SuiteResult suiteResult : runResult.getSuiteResults()) {
                if (currentSuiteResult.equals(suiteResult)) {
                    suiteFound = suiteResult;
                    break;
                }
            }

            for (TestResult testResult : suiteFound.getTests()) {
                if (!tests.contains(testResult)) {
                    res.addTest(testResult);
                }
            }
        }
        return res;
    }
}
