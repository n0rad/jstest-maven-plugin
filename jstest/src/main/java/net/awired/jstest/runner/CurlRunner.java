package net.awired.jstest.runner;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.awired.jscoverage.instrumentation.JsInstrumentor;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.server.RunnerResourceHandler;
import org.antlr.stringtemplate.StringTemplate;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class CurlRunner extends Runner {

    public CurlRunner() {
        super(RunnerType.CURL);
    }

    @Override
    public void replaceTemplateVars(StringTemplate template) {
        template.setAttribute("testResources", buildTestResources(resolver));
        template.setAttribute("testsJsArray", buildTestsJsArray());
    }

    private String buildTestResources(ResourceResolver resolver) {
        StringBuilder res = new StringBuilder();
        if (runnerType.getAmdFile() != null) {
            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + runnerType.getAmdFile());
        }
        for (String testerResource : testType.getTesterResources()) {
            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + testerResource);
        }
        htmlResourceTranformer.appendTag(res,
                RunnerResourceHandler.RUNNER_RESOURCE_PATH + testType.getTesterManager());
        htmlResourceTranformer.appendTag(res,
                RunnerResourceHandler.RUNNER_RESOURCE_PATH + JsInstrumentor.JSCOV_FILE.substring(1));
        return res.toString();
    }

    private String buildTestsJsArray() {
        Map<String, File> filterSourcesKeys = resolver.FilterTestsKeys();
        try {
            Set<String> keySet = filterSourcesKeys.keySet();
            Collection<String> transform = Collections2.transform(keySet, new Function<String, String>() {
                public String apply(String input) {
                    if (input.endsWith(".js")) {
                        return input.substring(0, input.length() - 3);
                    }
                    return input;
                }
            });
            return mapper.writeValueAsString(transform);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build testsJsArray", e);
        }
    }

}
