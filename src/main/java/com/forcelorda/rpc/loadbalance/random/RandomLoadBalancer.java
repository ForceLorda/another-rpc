package com.forcelorda.rpc.loadbalance.random;

import java.util.List;
import java.util.Random;

import com.forcelorda.rpc.entity.RpcServiceURL;
import com.forcelorda.rpc.loadbalance.LoadBalancer;

public class RandomLoadBalancer implements LoadBalancer{

	@Override
	public RpcServiceURL getRpcServiceURL(List<RpcServiceURL> urls) {
		Random random = new Random(urls.size());
	    int index = random.nextInt();
		return urls.get(index);
	}

}
