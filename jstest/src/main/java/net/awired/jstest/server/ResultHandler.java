package net.awired.jstest.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.awired.jstest.result.SuiteResult;
import net.awired.jstest.result.TestResult;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Request;

public class ResultHandler {

    private final Log log;
    private ObjectMapper mapper = new ObjectMapper();

    public ResultHandler(Log log) {
        this.log = log;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (target.equals("/result/test")) {
            TestResult result = mapper.readValue(request.getInputStream(), TestResult.class);
            log.debug(result.toString());
        } else if (target.equals("/result/suite")) {
            SuiteResult result = mapper.readValue(request.getInputStream(), SuiteResult.class);
            log.info(result.toString());
        }
    }

}
