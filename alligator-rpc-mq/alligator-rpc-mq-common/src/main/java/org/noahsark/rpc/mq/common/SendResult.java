package org.noahsark.rpc.mq.common;

/**
 * εεΊη»ζ
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface SendResult {

    boolean isSuccess();

    void setSuccess(boolean success);

    String getMsgId();

    void setMsgId(String msgId);

}
