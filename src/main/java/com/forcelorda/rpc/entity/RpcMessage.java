package com.forcelorda.rpc.entity;

import lombok.Data;

@Data
public class RpcMessage {
	
	private MessageHeader header;
	
	private Object body;
	
	
	public static final String PONG = "PONG";
	
	public static final String PING = "PING";
}
