package com.forcelorda.rpc.loadbalance;

import java.util.List;

import com.forcelorda.rpc.entity.RpcServiceURL;

public interface LoadBalancer {

	RpcServiceURL getRpcServiceURL(List<RpcServiceURL> urls);

}
