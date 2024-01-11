package com.forcelorda.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.RequestMapping;

import com.forcelorda.rpc.entity.RpcMethod;
import com.forcelorda.rpc.mapping.ServiceMapping;

public class RpcInvocationHandler implements InvocationHandler {

	private ServiceMapping serviceMapping;

	public RpcInvocationHandler(ServiceMapping serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		String[] paths = requestMapping.value();
		RpcMethod handlerMethod = null;
		for (String path : paths) {
			handlerMethod = serviceMapping.getHandlerMethod(path);
			if (handlerMethod != null) {
				break;
			}
		}
		return handlerMethod.getMethod().invoke(handlerMethod.getBean(), args);
	}

}
