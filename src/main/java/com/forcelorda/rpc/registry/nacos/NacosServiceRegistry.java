package com.forcelorda.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.forcelorda.rpc.constants.NacosConstant;
import com.forcelorda.rpc.entity.RpcServiceURL;
import com.forcelorda.rpc.registry.ServiceRegistry;

public class NacosServiceRegistry implements ServiceRegistry {

	private NamingService naming;

	private RpcServiceURL url;

	public void init() {
		try {
			this.naming = NamingFactory.createNamingService(System.getProperty(NacosConstant.NACOS_SERVER_ADDRESS));
		} catch (NacosException e) {
			e.printStackTrace();
		}
	}

	public void registerService(RpcServiceURL url) throws Exception {
		naming.registerInstance(url.getServiceName(), url.getIp(), url.getPort(), NacosConstant.GROUP_NAME);
	}

	public void destory() throws Exception {
		naming.deregisterInstance(url.getServiceName(), url.getIp(), url.getPort(), NacosConstant.GROUP_NAME);

	}

}
