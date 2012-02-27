package net.awired.jstest.result;

public class TestResult {

    private String name;
    private ResultType resultType;
    private long duration;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Test '");
        builder.append(name);
        builder.append("' result : ");
        builder.append(resultType);
        builder.append(" duration : ");
        builder.append(duration);
        builder.append("ms");
        return builder.toString();
    }

    /////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
