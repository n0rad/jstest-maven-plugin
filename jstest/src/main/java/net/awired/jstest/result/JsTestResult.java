package net.awired.jstest.result;

import java.util.HashMap;

public class JsTestResult extends HashMap<Integer, RunResult> {

    private static final long serialVersionUID = 1L;

    public boolean isFullyFinished() {
        if (this.size() == 0) {
            return false;
        }
        for (RunResult res : this.values()) {
            if (!res.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (RunResult results : this.values()) {
            builder.append(results.toString());
        }
        return builder.toString();
    }

}
