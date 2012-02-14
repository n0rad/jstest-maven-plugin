package net.awired.jstest.runner;

import java.io.File;
import java.util.Map;
import net.awired.jstest.resource.ResourceResolver;

public class HtmlResourceTranformer {

    public String buildTests(ResourceResolver resolver) {
        StringBuilder res = new StringBuilder();
        Map<String, File> filterSourcesKeys = resolver.FilterTestsKeys();
        for (String key : filterSourcesKeys.keySet()) {
            appendTag(res, key);
        }
        return res.toString();
    }

    public String buildSources(ResourceResolver resolver) {
        StringBuilder res = new StringBuilder();
        Map<String, File> filterSourcesKeys = resolver.FilterSourcesKeys();
        for (String key : filterSourcesKeys.keySet()) {
            appendTag(res, key);
        }
        return res.toString();
    }

    public void appendTag(StringBuilder builder, String path) {
        if (path.endsWith(".js")) {
            builder.append("<script type=\"text/javascript\" src=\"");
            builder.append(path);
            builder.append("\"></script>\n");
        } else if (path.endsWith(".css")) {
            builder.append("<link href=\"");
            builder.append(path);
            builder.append("\" rel=\"stylesheet\" type=\"text/css\">\n");
        }
    }
}
