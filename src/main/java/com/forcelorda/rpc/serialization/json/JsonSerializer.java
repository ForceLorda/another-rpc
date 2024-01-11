package com.forcelorda.rpc.serialization.json;

import com.alibaba.fastjson2.JSONObject;
import com.forcelorda.rpc.exception.SerializationException;
import com.forcelorda.rpc.serialization.Serializer;

public class JsonSerializer implements Serializer {

	public byte[] serialize(Object obj) throws SerializationException {
		return JSONObject.from(obj).toString().getBytes();
	}

	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
		return JSONObject.parseObject(new String(bytes), clazz);
	}

}
