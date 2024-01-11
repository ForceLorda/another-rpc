package com.forcelorda.rpc.spring.server;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.forcelorda.rpc.entity.RpcServiceURL;
import com.forcelorda.rpc.mapping.DefaultServiceMapping;
import com.forcelorda.rpc.mapping.ServiceMapping;
import com.forcelorda.rpc.registry.ServiceRegistry;
import com.forcelorda.rpc.registry.nacos.NacosServiceRegistry;
import com.forcelorda.rpc.remoting.netty.server.NettyRpcServer;
import com.forcelorda.rpc.remoting.server.RpcServer;

@Configuration
public class RpcServerAutoConfiguration {
	
	@Value("spring.application.name")
	String name;
	/**
	 * 服务端
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(RpcServer.class)
	public RpcServer rpcServer() {
		return new NettyRpcServer(8899);
	}

	/**
	 * 服务注册
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod = "destory")
	@ConditionalOnMissingBean(ServiceRegistry.class)
	public ServiceRegistry serviceRegistry() throws Exception {
		ServiceRegistry serviceRegistry = new NacosServiceRegistry();
		serviceRegistry.init();
		RpcServiceURL url = new RpcServiceURL();
		url.setServiceName(name);
		String ip = InetAddress.getLocalHost().getHostAddress();
		url.setIp(ip);
		url.setPort(8899);
		serviceRegistry.registerService(url);
		return serviceRegistry;
	}

	/**
	 * 映射注册
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ServiceMapping.class)
	public ServiceMapping rpcServerProvider() {
		return new DefaultServiceMapping();
	}
}
