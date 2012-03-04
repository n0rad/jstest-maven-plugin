package net.awired.jstest.result;

import java.util.ArrayList;
import java.util.List;
import nl.bitwalker.useragentutils.UserAgent;

public class RunResult {

    private final List<SuiteResult> suiteResults = new ArrayList<SuiteResult>();
    private Long duration;
    private UserAgent userAgent;
    private boolean emulator;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nResults :\n\n");
        builder.append("JsTests run: ");
        builder.append(findTests());
        builder.append(", Failures: ");
        builder.append(findFailures());
        builder.append(", Errors: ");
        builder.append(findErrors());
        builder.append(", Skipped: ");
        builder.append(findSkipped());
        builder.append(", Duration: ");
        builder.append(duration);
        builder.append("ms");
        if (!emulator) {
            builder.append(", Agent: ");
            builder.append(userAgentToString());
        }
        builder.append("\n");
        return builder.toString();
    }

    public String userAgentToString() {
        return userAgent.getOperatingSystem().getName() //
                + ',' + userAgent.getBrowser().getName() //
                + ' ' + userAgent.getBrowserVersion();
    }

    public void addSuite(SuiteResult suiteResult) {
        suiteResults.add(suiteResult);
    }

    private int findTests() {
        int tests = 0;
        for (SuiteResult suiteResult : suiteResults) {
            tests += suiteResult.getTests().size();
        }
        return tests;
    }

    private int findFailures() {
        int failures = 0;
        for (SuiteResult suiteResult : suiteResults) {
            failures += suiteResult.findFailures();
        }
        return failures;
    }

    private int findErrors() {
        int errors = 0;
        for (SuiteResult suiteResult : suiteResults) {
            errors += suiteResult.findErrors();
        }
        return errors;
    }

    private int findSkipped() {
        int skipped = 0;
        for (SuiteResult suiteResult : suiteResults) {
            skipped += suiteResult.findSkipped();
        }
        return skipped;
    }

    private long findAggregatedDuration() {
        long duration = 0;
        for (SuiteResult suiteResult : suiteResults) {
            duration += suiteResult.findDuration();
        }
        return duration;
    }

    public boolean isFinished() {
        return duration != null;
    }

    //////////////////////////////:

    public List<SuiteResult> getSuiteResults() {
        return suiteResults;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getDuration() {
        return duration;
    }

    public void setBrowserType(String browserType) {
        userAgent = UserAgent.parseUserAgentString(browserType);
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }

    public void setEmulator(boolean emulator) {
        this.emulator = emulator;
    }

    public boolean isEmulator() {
        return emulator;
    }

}
