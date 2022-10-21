package org.noahsark.rpc.socket.tcp;

import org.junit.Test;
import org.noahsark.rpc.socket.tcp.server.TcpServer;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class TcpServerTest {

    @Test
    public void tcpServerTest() {
        String host = "192.168.66.83";
        int port = 2222;

        final TcpServer tcpServer = new TcpServer(host, port);
        //tcpServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                tcpServer.shutdown();
            }
        });

        tcpServer.start();

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
