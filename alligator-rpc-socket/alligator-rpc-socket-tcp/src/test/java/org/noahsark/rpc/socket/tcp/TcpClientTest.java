package org.noahsark.rpc.socket.tcp;

import org.junit.Test;
import org.noahsark.rpc.socket.tcp.client.TcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class TcpClientTest {

    @Test
    public void tcpClientTest() {
        TcpClient tcpClient = new TcpClient("192.168.66.83", 2222);
        tcpClient.connect();

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tcpMultiServerTest() {
        List<String> urls = new ArrayList<>();

        urls.add("192.168.9.103:2222");
        urls.add("192.168.9.103:2223");

        TcpClient tcpClient = new TcpClient(urls);
        tcpClient.connect();

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
