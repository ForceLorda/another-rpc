package com.forcelorda.rpc.proxy;

import java.lang.reflect.Proxy;

import com.forcelorda.rpc.mapping.ServiceMapping;

public class RpcProxy {
	
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance( Class<T> clazz,ServiceMapping serviceMapping) {
		
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new RpcInvocationHandler(serviceMapping));
	}
}
