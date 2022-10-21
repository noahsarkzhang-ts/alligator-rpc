package org.noahsark.rpc.socket.ws.server;

import org.junit.Test;
import org.noahsark.rpc.common.constant.RpcCommandType;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.util.JsonUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class WebSocketServerTest {

    @Test
    public void testServer() {
        String host = "192.168.66.83";
        int port = 9090;

        // 请求
        // request = {"className":"inviter","method":"login","requestId":1,"version":"V1.0","payload":{"userName":"allan","password":"test"}}

        final WebSocketServer webSocketServer = new WebSocketServer(host, port);
        // webSocketServer.init();

        UserLoginProcessor processor = new UserLoginProcessor();
        processor.register();

        UsersProcessor usersProcessor = new UsersProcessor();
        usersProcessor.register();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                webSocketServer.shutdown();
            }
        });

        webSocketServer.start();

        try {
            TimeUnit.MINUTES.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class UserInfo {

        private String userId;

        private String userName;

        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static class TokenInfo {
        private String userName;

        private String token;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "TokenInfo{" +
                    "userName='" + userName + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

    private static class UserLoginProcessor extends AbstractProcessor<UserInfo> {

        @Override
        protected void execute(UserInfo request, RpcContext context) {

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserName(request.getUserName());
            tokenInfo.setToken(UUID.randomUUID().toString());

            Response response = Response.buildResponse(context.getCommand(), RpcCommandType.RESPONSE,
                    tokenInfo, 1, "success");

            String text = JsonUtils.toJson(response);
            System.out.println("text = " + text);

            context.sendResponse(response);
        }

        @Override
        protected Class<UserInfo> getParamsClass() {
            return UserInfo.class;
        }

        @Override
        protected int getBiz() {
            return 1;
        }

        @Override
        protected int getCmd() {
            return 1000;
        }

    }

    private static class UsersProcessor extends AbstractProcessor<Void> {

        @Override
        protected void execute(Void request, RpcContext context) {

            // 按照流的方式返回数据

            UserInfo userInfo = new UserInfo();
            userInfo.setUserId("1002");
            userInfo.setUserName("allen");
            userInfo.setPassword("allen");

            Response response = Response.buildStream(context.getCommand(), userInfo, 1, "success");

            // 发送第一批数据
            context.flow(response);

            userInfo = new UserInfo();
            userInfo.setUserId("1003");
            userInfo.setUserName("hunter");
            userInfo.setPassword("hunter");

            response = Response.buildStream(context.getCommand(), userInfo, 1, "success");

            // 发送最后一批数据
            context.end(response);
        }

        @Override
        protected Class<Void> getParamsClass() {
            return Void.class;
        }

        @Override
        protected int getBiz() {
            return 1;
        }

        @Override
        protected int getCmd() {
            return 1001;
        }
    }
}
