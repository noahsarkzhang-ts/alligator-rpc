package org.noahsark.rpc.common.serializer;

import org.noahsark.rpc.common.constant.SerializerType;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化管理类
 *
 * @author zhangxt
 * @date 2021/4/2
 */
public class SerializerManager {
    private Map<Byte, Serializer> serializerMap = new HashMap<>();

    private static class SerializerManagerHolder {
        private static final SerializerManager INSTANCE = new SerializerManager();
    }

    private SerializerManager() {
        serializerMap.put(SerializerType.JSON, new JsonSerializer());
    }

    public static SerializerManager getInstance() {
        return SerializerManagerHolder.INSTANCE;
    }

    public void register(Byte type, Serializer serializer) {
        serializerMap.put(type, serializer);
    }

    public Serializer getSerializer(byte type) {
        return serializerMap.get(type);
    }
}
