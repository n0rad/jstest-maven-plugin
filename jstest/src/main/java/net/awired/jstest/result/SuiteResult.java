package net.awired.jstest.result;

import java.util.ArrayList;
import java.util.List;

public class SuiteResult {

    private String name;
    private Integer failures;
    private Integer errors;
    private Integer skipped;
    private Long duration;
    private Long suiteDuration;

    private List<TestResult> tests = new ArrayList<TestResult>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Run suite : ");
        builder.append(name);
        builder.append("\n");
        builder.append("Tests run: ");
        builder.append(tests.size());
        builder.append(", Failures: ");
        builder.append(findFailures());
        builder.append(", Errors: ");
        builder.append(findErrors());
        builder.append(", Skipped: ");
        builder.append(findSkipped());
        builder.append(", Time elapsed: ");
        builder.append(findDuration());
        builder.append(" ms");
        return builder.toString();
    }

    public long findDuration() {
        if (duration == null) {
            duration = 0L;
            for (TestResult result : tests) {
                duration += result.getDuration();
            }
        }
        return duration;
    }

    public int findSkipped() {
        if (skipped == null) {
            skipped = 0;
            for (TestResult result : tests) {
                if (result.getResultType() == ResultType.skipped) {
                    skipped++;
                }
            }
        }
        return skipped;
    }

    public int findErrors() {
        if (errors == null) {
            errors = 0;
            for (TestResult result : tests) {
                if (result.getResultType() == ResultType.error) {
                    errors++;
                }
            }
        }
        return errors;
    }

    public int findFailures() {
        if (failures == null) {
            failures = 0;
            for (TestResult result : tests) {
                if (result.getResultType() == ResultType.failure) {
                    failures++;
                }
            }
        }
        return failures;
    }

    public void addTests(List<TestResult> currentTests) {
        tests.addAll(currentTests);
    }

    //////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTests(List<TestResult> tests) {
        this.tests = tests;
    }

    public List<TestResult> getTests() {
        return tests;
    }

}
