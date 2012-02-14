package net.awired.jstest.runner;

public enum AmdRunnerType {
    NONE("/runnerTemplate/defaultRunner.tpl"), //
    REQUIREJS("/runnerTemplate/requireRunner.tpl", "/require.js"), //
    CURL("/runnerTemplate/curlRunner.tpl", "/curl.js"), //
    ;

    private String template;
    private String amdFile;

    private AmdRunnerType(String template) {
        this.setTemplate(template);
    }

    private AmdRunnerType(String template, String amdFile) {
        this.setAmdFile(amdFile);
        this.setTemplate(amdFile);
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

}
