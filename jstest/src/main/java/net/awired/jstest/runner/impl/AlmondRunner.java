package net.awired.jstest.runner.impl;

import net.awired.jstest.runner.RunnerType;

public class AlmondRunner extends DefaultRunner {

    public AlmondRunner() {
        this.runnerType = RunnerType.ALMOND;
    }

    //    @Override
    //    public void replaceTemplateVars(StringTemplate template) throws Exception {
    //        template.setAttribute("testResources", buildTestResources(resolver));
    //        template.setAttribute("sources", htmlResourceTranformer.buildTagsFromResources(resolver.FilterSourcesKeys()));
    //        template.setAttribute("tests", htmlResourceTranformer.buildTagsFromResources(resolver.FilterTestsKeys()));
    //    }
    //
    //    private String buildTestResources(ResourceResolver resolver) {
    //        StringBuilder res = new StringBuilder();
    //        if (runnerType.getAmdFile() != null) {
    //            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + runnerType.getAmdFile());
    //        }
    //        for (String testerResource : testType.getTesterResources()) {
    //            htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + testerResource);
    //        }
    //        //        htmlResourceTranformer.appendTag(res, ResourceResolver.SRC_RESOURCE_PREFIX + "build/firebug-lite.js");
    //        htmlResourceTranformer.appendTag(res,
    //                RunnerResourceHandler.RUNNER_RESOURCE_PATH + testType.getTesterManager());
    //        htmlResourceTranformer.appendTag(res,
    //                RunnerResourceHandler.RUNNER_RESOURCE_PATH + JsInstrumentor.JSCOV_FILE.substring(1));
    //        return res.toString();
    //    }

    //    private String buildTestsJsArray() {
    //        Map<String, File> filterSourcesKeys = resolver.FilterTestsKeys();
    //        try {
    //            Set<String> keySet = filterSourcesKeys.keySet();
    //
    //            Function<String, String> withoutPrefix = new Function<String, String>() {
    //                @Override
    //                public String apply(String input) {
    //                    return input.substring(ResourceResolver.TEST_RESOURCE_PREFIX.length());
    //                }
    //            };
    //
    //            Predicate<String> filterJsFiles = new Predicate<String>() {
    //                @Override
    //                public boolean apply(String input) {
    //                    return input.toLowerCase().endsWith(".js");
    //                }
    //            };
    //
    //            Collection<String> amdModules = Collections2.transform(Collections2.filter(keySet, filterJsFiles),
    //                    withoutPrefix);
    //            return mapper.writeValueAsString(amdModules);
    //        } catch (Exception e) {
    //            throw new RuntimeException("Cannot build testsJsArray", e);
    //        }
    //    }

}
