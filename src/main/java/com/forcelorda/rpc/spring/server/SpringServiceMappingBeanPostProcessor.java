package com.forcelorda.rpc.spring.server;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forcelorda.rpc.entity.RpcMethod;
import com.forcelorda.rpc.mapping.DefaultServiceMapping;
@Component
public class SpringServiceMappingBeanPostProcessor extends DefaultServiceMapping implements BeanPostProcessor {
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean.getClass().isAnnotationPresent(RestController.class)) {

			RequestMapping classRequestMapping = bean.getClass().getAnnotation(RequestMapping.class);
			if (null == classRequestMapping) {
				Method[] methods = bean.getClass().getMethods();
				for (Method method : methods) {
					Set<String> urls = new HashSet<>();
					RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
					String[] methodMappings = methodRequestMapping.value();
					for (String methodMapping : methodMappings) {
						urls.add(methodMapping);
					}
					RpcMethod rpcMethod = new RpcMethod(bean, method);
					registerServiceMapping(urls, rpcMethod);
				}

			} else {
				Method[] methods = bean.getClass().getMethods();
				String[] classMappings = classRequestMapping.value();
				for (String classMapping : classMappings) {
					for (Method method : methods) {
						Set<String> urls = new HashSet<>();
						RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
						String[] methodMappings = methodRequestMapping.value();
						for (String methodMapping : methodMappings) {
							if (classMapping.equals("/")) {
								urls.add(methodMapping);
							} else {
								urls.add(classMapping + methodMapping);
							}

						}
						RpcMethod rpcMethod = new RpcMethod(bean, method);
						registerServiceMapping(urls, rpcMethod);
					}
				}
			}

		}
		return bean;
	}
}
