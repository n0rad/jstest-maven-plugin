package net.awired.jstest.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringStacktrace {

	public String stringify(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		t.printStackTrace(printWriter);
		return stringWriter.toString();
	}

}
