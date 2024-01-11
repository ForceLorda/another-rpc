package com.forcelorda.rpc.entity;

import lombok.Data;

@Data
public class RpcRequest {
	
	private String group;
	
	private String url;
	
	private String version;
	
	private Object[] parameters;
	
    private Class<?>[] paramTypes;
    
}
