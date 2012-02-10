package net.awired.jstest.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

public class JsTestServer {
    private static final int MIN_PORT = 10000;
    private static final int MAX_PORT = 65534;

    private final Log log;

    private Server server = new Server();
    private int port;

    public JsTestServer(Log log) {
        this.log = log;
        int tmpPort;
        do {
            tmpPort = MIN_PORT + (int) (Math.random() * ((MAX_PORT - MIN_PORT) + 1));
        } while (!available(tmpPort));
        this.port = tmpPort;
    }

    public JsTestServer(Log log, int port) {
        this.log = log;
        this.port = port;
    }

    public void startServer(Handler handler) throws Exception {
        SelectChannelConnector connector = new SelectChannelConnector();
        server.addConnector(connector);
        server.setHandler(handler);
        connector.setPort(port);
        log.info("Starting JsTest server on port " + port);
        server.start();
    }

    public void join() {
        log.debug("joining JsTest server");
        try {
            server.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() {
        server.destroy();
    }

    public static boolean available(int port) {
        if (port < 0 || port > 0xFFFF) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

}
