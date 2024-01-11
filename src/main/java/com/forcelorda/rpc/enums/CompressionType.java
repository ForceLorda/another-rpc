package com.forcelorda.rpc.enums;

public enum CompressionType {

	GZIP(0);
	
	private int code;

	private CompressionType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public static CompressionType findByCode(byte code) {
        for (CompressionType type : CompressionType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
