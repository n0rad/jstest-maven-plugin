package net.awired.jstest.server.handler;

import net.awired.jscoverage.result.CoverageResult;

class PartialRunResult {
    private long duration;
    private CoverageResult coverageResult;

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setCoverageResult(CoverageResult coverageResult) {
        this.coverageResult = coverageResult;
    }

    public CoverageResult getCoverageResult() {
        return coverageResult;
    }
}
