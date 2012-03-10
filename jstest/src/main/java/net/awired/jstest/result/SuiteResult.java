package net.awired.jstest.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.NONE)
public class SuiteResult {

    private RunResult runResult;

    @XmlAttribute
    private String name;

    private long duration;

    private Integer failures;
    private Integer errors;
    private Integer skipped;

    @XmlElement(name = "testcase")
    private List<TestResult> tests = new ArrayList<TestResult>();

    @XmlAttribute(name = "tests")
    public int getTestSize() {
        return tests.size();
    }

    @XmlAttribute(name = "time")
    public double getTime() {
        return duration / 1000.0;
    }

    @XmlAttribute(name = "skipped")
    public int getSkipped() {
        if (skipped == null) {
            skipped = 0;
            for (TestResult result : tests) {
                if (result.isSuccess()) {
                    skipped++;
                }
            }
        }
        return skipped;
    }

    @XmlAttribute(name = "errors")
    public int getErrors() {
        if (errors == null) {
            errors = 0;
            for (TestResult result : tests) {
                if (result.isError()) {
                    errors++;
                }
            }
        }
        return errors;
    }

    @XmlAttribute(name = "failures")
    public int getFailures() {
        if (failures == null) {
            failures = 0;
            for (TestResult result : tests) {
                if (result.isFailure()) {
                    failures++;
                }
            }
        }
        return failures;
    }

    public void addTest(TestResult test) {
        resetStats();
        tests.add(test);
    }

    public void addTests(List<TestResult> currentTests) {
        resetStats();
        tests.addAll(currentTests);
    }

    public List<TestResult> getTests() {
        return Collections.unmodifiableList(tests);
    }

    public void setTests(List<TestResult> tests) {
        resetStats();
        this.tests = tests;
    }

    private void resetStats() {
        failures = null;
        errors = null;
        skipped = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuiteResult) {
            final SuiteResult other = (SuiteResult) obj;
            return Objects.equal(name, other.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    //////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }

    public RunResult getRunResult() {
        return runResult;
    }

}
