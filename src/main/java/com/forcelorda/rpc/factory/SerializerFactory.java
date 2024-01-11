package com.forcelorda.rpc.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.forcelorda.rpc.enums.SerializationType;
import com.forcelorda.rpc.serialization.Serializer;
import com.forcelorda.rpc.serialization.json.JsonSerializer;

public class SerializerFactory {
private static ConcurrentHashMap<SerializationType, Serializer> map = new ConcurrentHashMap<>();
	
	static {
		map.put(SerializationType.JSON, new JsonSerializer());
	}
	
	public static Serializer getSerializer(SerializationType serializationType) {
		return map.get(serializationType);
	}
}
