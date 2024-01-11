package com.forcelorda.rpc.discovery;

import com.forcelorda.rpc.entity.RpcServiceURL;

public interface ServiceDiscovery {
	
	void init();	
	
	RpcServiceURL getService();
}
