package com.forcelorda.rpc.mapping;

import java.util.Set;

import com.forcelorda.rpc.entity.RpcMethod;

public interface ServiceMapping {
	RpcMethod getHandlerMethod(String requestMapping);
	
	void registerServiceMapping(Set<String> url,RpcMethod handlerMethod);
}
