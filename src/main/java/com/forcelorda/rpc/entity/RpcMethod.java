package com.forcelorda.rpc.entity;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class RpcMethod {
	private Object bean;
	private Method method;
}
