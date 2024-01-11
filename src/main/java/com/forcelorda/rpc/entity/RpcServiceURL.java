package com.forcelorda.rpc.entity;

import lombok.Data;

@Data
public class RpcServiceURL {
	
	private String serviceName;
	private String ip;
	private int port;
}
