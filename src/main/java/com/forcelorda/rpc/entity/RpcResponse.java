package com.forcelorda.rpc.entity;

import com.forcelorda.rpc.enums.RpcResponseCodeEnum;

import lombok.Data;

@Data
public class RpcResponse {
	
    private Integer code;
    
    private String message;
    
    private Object data;
    
    private Exception exception;

    public static RpcResponse ok(Object data) {
        RpcResponse response = new RpcResponse();
        response.setCode(RpcResponseCodeEnum.OK.getCode());
        response.setMessage(RpcResponseCodeEnum.OK.name());
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static RpcResponse error(RpcResponseCodeEnum rpcResponseCodeEnum,Exception exception) {
        RpcResponse response = new RpcResponse();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        response.setException(exception);
        return response;
    }

}
