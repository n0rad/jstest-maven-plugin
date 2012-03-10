package net.awired.jstest.result;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RunResultsTest {

    @Test
    public void should_aggregate_results() {
        RunResults results = new RunResults();

        {
            RunResult runResult = new RunResult(0);
            runResult.setDuration(49L);
            SuiteResult suiteResult = new SuiteResult();
            suiteResult.setName("yopla");
            suiteResult.setDuration(34L);
            TestResult test = new TestResult();
            test.setName("test1");
            test.setDuration(44);
            suiteResult.addTest(test);
            TestResult test2 = new TestResult();
            test2.setName("test2");
            test2.setDuration(45);
            ErrorDescription error = new ErrorDescription();
            error.setMessage("error of test2");
            test2.setError(error);
            suiteResult.addTest(test2);
            runResult.addSuite(suiteResult);
            results.put(0, runResult);
        }

        {
            RunResult runResult = new RunResult(1);
            runResult.setDuration(47L);
            SuiteResult suiteResult = new SuiteResult();
            suiteResult.setName("yopla");
            suiteResult.setDuration(33L);
            TestResult test = new TestResult();
            test.setName("test1");
            test.setDuration(44);
            suiteResult.addTest(test);
            TestResult test2 = new TestResult();
            test2.setName("test2");
            test2.setDuration(45);
            suiteResult.addTest(test2);
            TestResult test3 = new TestResult();
            test3.setName("test3");
            test3.setDuration(46);
            suiteResult.addTest(test3);
            runResult.addSuite(suiteResult);
            results.put(1, runResult);
        }

        RunResult aggregatedResult = results.buildAggregatedResult();

        assertEquals(49L, aggregatedResult.getDuration());
        assertEquals(1, aggregatedResult.getSuiteResults().size());
        SuiteResult suiteResult = aggregatedResult.getSuiteResults().get(0);
        assertEquals("yopla", suiteResult.getName());
        assertEquals(34L, suiteResult.getDuration());
        assertEquals(3, suiteResult.getTests().size());
        assertEquals("test2", suiteResult.getTests().get(0).getName());
        assertEquals(true, suiteResult.getTests().get(0).isError());
        assertEquals("test1", suiteResult.getTests().get(1).getName());
        assertEquals(true, suiteResult.getTests().get(1).isSuccess());
        assertEquals("test3", suiteResult.getTests().get(2).getName());
        assertEquals(true, suiteResult.getTests().get(2).isSuccess());
    }
}
