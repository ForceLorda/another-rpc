package com.forcelorda.rpc.enums;

public enum SerializationType {
	
	JSON(0);
	
	private int code;

	private SerializationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static SerializationType findByCode(byte code) {
        for (SerializationType type : SerializationType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
	
}
