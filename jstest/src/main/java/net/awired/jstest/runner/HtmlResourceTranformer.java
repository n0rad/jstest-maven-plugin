package net.awired.jstest.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import net.awired.jstest.resource.ResourceResolver;
import com.google.common.io.CharStreams;

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

    public void appendSourceTag(StringBuilder builder, File file) {
        try {
            appendSourceTag(builder, new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot open source file : " + file, e);
        }
    }

    public void appendSourceTag(StringBuilder builder, InputStream in, String filename) {
        String sourceCode;
        try {
            sourceCode = CharStreams.toString(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot open source file : " + filename, e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open source file : " + filename, e);
        }
        if (filename.endsWith(".js")) {
            builder.append("<script type=\"text/javascript\">");
            builder.append(sourceCode);
            builder.append("</script>\n");
        } else if (filename.endsWith(".css")) {
            builder.append("<style type=\"text/css\">");
            builder.append(sourceCode);
            builder.append("</style>\n");
        }
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
