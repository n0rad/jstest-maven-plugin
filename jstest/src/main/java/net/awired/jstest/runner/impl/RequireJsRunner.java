package net.awired.jstest.runner.impl;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.awired.jscoverage.instrumentation.JsInstrumentor;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.runner.Runner;
import net.awired.jstest.runner.RunnerType;
import net.awired.jstest.server.handler.RunnerResourceHandler;
import org.antlr.stringtemplate.StringTemplate;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class RequireJsRunner extends Runner {

    public RequireJsRunner() {
        super(RunnerType.REQUIREJS);
    }

    @Override
    public void replaceTemplateVars(StringTemplate template) throws Exception {
        template.setAttribute("testResources", buildTestResources(resolver));
        template.setAttribute("testsJsArray", buildTestsJsArray());
        template.setAttribute("amdPreload", mapper.writeValueAsString(amdPreloads));
    }

    private String buildTestResources(ResourceResolver resolver) {
        StringBuilder res = new StringBuilder();
        if (runnerType.getAmdFile() != null) {
            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + runnerType.getAmdFile());
        }
        for (String testerResource : testType.getTesterResources()) {
            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + testerResource);
        }
        //        htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + "build/firebug-lite.js");
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

            Predicate<String> filterJsFiles = new Predicate<String>() {
                @Override
                public boolean apply(String input) {
                    return input.toLowerCase().endsWith(".js");
                }
            };

            Collection<String> amdModules = Collections2.filter(keySet, filterJsFiles);
            return mapper.writeValueAsString(amdModules);
        } catch (Exception e) {
            throw new RuntimeException("Cannot build testsJsArray", e);
        }
    }

}
