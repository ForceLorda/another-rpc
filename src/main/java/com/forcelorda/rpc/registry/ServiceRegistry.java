package com.forcelorda.rpc.registry;

import com.forcelorda.rpc.entity.RpcServiceURL;

public interface ServiceRegistry {
	
	void init();
	void registerService(RpcServiceURL url) throws Exception;
	
	void destory() throws Exception;
}
