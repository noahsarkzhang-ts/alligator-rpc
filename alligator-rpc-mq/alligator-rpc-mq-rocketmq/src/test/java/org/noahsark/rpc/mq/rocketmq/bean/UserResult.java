package org.noahsark.rpc.mq.rocketmq.bean;

/**
 * 用户处理结果
 *
 * @author zhangxt
 * @date 2022/10/17 09:57
 **/
public class UserResult {

    private String userId;

    private byte status;

    public UserResult(String userId, byte status) {
        this.userId = userId;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "userId='" + userId + '\'' +
                ", status=" + status +
                '}';
    }
}
