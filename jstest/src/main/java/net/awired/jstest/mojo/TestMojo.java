package net.awired.jstest.mojo;

import net.awired.jstest.mojo.inherite.AbstractJsTestMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @component
 * @goal test
 * @phase test
 * @execute lifecycle="jstest-lifecycle" phase="process-test-resources"
 */
public class TestMojo extends AbstractJsTestMojo {

    @Override
    public void run() throws MojoExecutionException, MojoFailureException {
        System.out.println("running test");
    }

}
