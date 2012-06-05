package net.awired.jstest.runner;

import java.util.List;
import net.awired.jstest.resource.ResourceResolver;
import net.awired.jstest.runner.impl.CurlRunner;
import net.awired.jstest.runner.impl.DefaultRunner;

public enum RunnerType {
    DEFAULT(DefaultRunner.class, "/runnerTemplate/defaultRunner.tpl"), //
    //    REQUIREJS(RequireJsRunner.class, "/runnerTemplate/requireRunner.tpl", "require.js"), //
    CURL(CurlRunner.class, "/runnerTemplate/curlRunner.tpl", "curl.js"), //
    ;

    private String template;
    private String amdFile;
    private final Class<? extends Runner> runnerClass;

    private RunnerType(Class<? extends Runner> runnerClass, String template) {
        this.runnerClass = runnerClass;
        this.template = template;
    }

    private RunnerType(Class<? extends Runner> runnerClass, String template, String amdFile) {
        this.runnerClass = runnerClass;
        this.template = template;
        this.amdFile = amdFile;
    }

    ///////////////////

    public void setAmdFile(String amdFile) {
        this.amdFile = amdFile;
    }

    public String getAmdFile() {
        return amdFile;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public Runner buildRunner(TestType testType, ResourceResolver resolver, boolean serverMode, boolean debug,
            List<String> amdPreloads) {
        try {
            Runner runner = runnerClass.newInstance();
            runner.setTestType(testType);
            runner.setResolver(resolver);
            runner.setDebug(debug);
            runner.setServerMode(serverMode);
            runner.setAmdPreloads(amdPreloads);
            return runner;
        } catch (Exception e) {
            throw new RuntimeException("Cannot instanciate runner", e);
        }
    }
}
