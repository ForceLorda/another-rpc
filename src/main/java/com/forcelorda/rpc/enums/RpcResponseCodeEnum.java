package com.forcelorda.rpc.enums;


public enum RpcResponseCodeEnum {
	OK(200,"成功"),ERROR(500,"失败");
	
	
	private int code;
	
	private String message;
	
	RpcResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
