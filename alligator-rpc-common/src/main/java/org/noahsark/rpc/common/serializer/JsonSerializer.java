package org.noahsark.rpc.common.serializer;

import org.noahsark.rpc.common.util.JsonUtils;

import java.nio.charset.Charset;

/**
 * JSON 序列化类
 * @author zhangxt
 * @date 2021/4/2
 */
public class JsonSerializer implements Serializer {

    private Charset charset = Charset.forName("UTF-8");

    @Override
    public byte[] encode(Object obj) {

        String json = JsonUtils.toJson(obj);

        return json.getBytes(charset);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> classz) {

        String json = new String(bytes,charset);

        return JsonUtils.fromJson(json,classz);
    }

}
