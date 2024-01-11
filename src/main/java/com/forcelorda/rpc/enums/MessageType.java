package com.forcelorda.rpc.enums;

public enum MessageType {
	
	REQUEST(0),RESPONSE(1),HEARTBEAT(2);
	
	private int code;

	private MessageType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static MessageType findByCode(byte code) {
        for (MessageType type : MessageType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
