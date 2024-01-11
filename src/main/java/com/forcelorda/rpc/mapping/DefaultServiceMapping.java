package com.forcelorda.rpc.mapping;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.forcelorda.rpc.entity.RpcMethod;

public class DefaultServiceMapping implements ServiceMapping{

	private ConcurrentHashMap<Set<String>, RpcMethod> map = new ConcurrentHashMap<>(); 
	
	@Override
	public RpcMethod getHandlerMethod(String requestMapping) {
		for (Entry<Set<String>, RpcMethod> entry : map.entrySet()) {
			Set<String> key = entry.getKey();
			if (key.contains(requestMapping)) {
				return entry.getValue();
			}	
		}
		return null;
	}

	@Override
	public void registerServiceMapping(Set<String> url,RpcMethod handlerMethod) {
		map.put(url, handlerMethod);
		
	}

}
