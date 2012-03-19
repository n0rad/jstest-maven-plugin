package net.awired.jstest.runner.impl;

import net.awired.jscoverage.instrumentation.JsInstrumentor;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.runner.Runner;
import net.awired.jstest.runner.RunnerType;
import net.awired.jstest.server.handler.RunnerResourceHandler;
import org.antlr.stringtemplate.StringTemplate;

public class DefaultRunner extends Runner {

    public DefaultRunner() {
        super(RunnerType.DEFAULT);
    }

    @Override
    public void replaceTemplateVars(StringTemplate template) {
        template.setAttribute("testResources", buildTestResources(resolver));
        template.setAttribute("sources", htmlResourceTranformer.buildTagsFromResources(resolver.FilterSourcesKeys()));
        template.setAttribute("tests", htmlResourceTranformer.buildTagsFromResources(resolver.FilterTestsKeys()));
    }

    private String buildTestResources(ResourceResolver resolver) {
        StringBuilder res = new StringBuilder();
        htmlResourceTranformer.appendTag(res,
                RunnerResourceHandler.RUNNER_RESOURCE_PATH + testType.getTesterManager());
        htmlResourceTranformer.appendTag(res,
                RunnerResourceHandler.RUNNER_RESOURCE_PATH + JsInstrumentor.JSCOV_FILE.substring(1));
        return res.toString();
    }

}
