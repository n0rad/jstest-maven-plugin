package net.awired.jstest.server;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.result.RunResult;
import net.awired.jstest.result.SuiteResult;
import net.awired.jstest.result.TestResult;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Request;

public class ResultHandler {

    private static final String HEADER = "\n" //
            + "-------------------------------------------------------\n" //
            + " J S   T E S T S\n" //
            + "-------------------------------------------------------";

    private final Log log;
    private ObjectMapper mapper = new ObjectMapper();
    private PrintStream out = System.out;
    private RunResult runResult = new RunResult();

    public ResultHandler(Log log) {
        this.log = log;
        out.println(HEADER);
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (target.equals("/result/test")) {
            TestResult testResult = mapper.readValue(request.getInputStream(), TestResult.class);
            log.debug(testResult.toString());
        } else if (target.equals("/result/suite")) {
            SuiteResult suiteResult = mapper.readValue(request.getInputStream(), SuiteResult.class);
            runResult.addSuite(suiteResult);
            out.println(suiteResult);
        } else if (target.equals("/result/run")) {
            out.println(runResult);
        }
    }
}
