package net.awired.jstest.runner;

public enum TestType {
    JASMINE("jasmineManager.js", "jasmine.js", "jasmine-html.js", "jasmine.css"),
    //QUNIT
    ;

    private String[] testerResources;
    private String testerManager;

    private TestType(String testerManager, String... testerResources) {
        this.testerResources = testerResources;
        this.testerManager = testerManager;
    }

    public String getTesterManager() {
        return testerManager;
    }

    public void setTesterManager(String testerManager) {
        this.testerManager = testerManager;
    }

    public String[] getTesterResources() {
        return testerResources;
    }

    public void setTesterResources(String[] testerResources) {
        this.testerResources = testerResources;
    }
}
