package net.awired.jstest.result;

import java.util.List;

public class SuiteResult {

    private String name;
    private List<TestResult> tests;

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

    private Object findDuration() {
        int duration = 0;
        for (TestResult result : tests) {
            duration += result.getDuration();
        }
        return duration;
    }

    private int findSkipped() {
        int skipped = 0;
        for (TestResult result : tests) {
            if (result.getResultType() == ResultType.skipped) {
                skipped++;
            }
        }
        return skipped;
    }

    private int findErrors() {
        int errors = 0;
        for (TestResult result : tests) {
            if (result.getResultType() == ResultType.error) {
                errors++;
            }
        }
        return errors;
    }

    public int findFailures() {
        int failures = 0;
        for (TestResult result : tests) {
            if (result.getResultType() == ResultType.failure) {
                failures++;
            }
        }
        return failures;
    }

    //////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestResult> getTests() {
        return tests;
    }

    public void setTests(List<TestResult> tests) {
        this.tests = tests;
    }
}
