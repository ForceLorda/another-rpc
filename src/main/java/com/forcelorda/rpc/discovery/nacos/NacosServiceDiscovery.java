package com.forcelorda.rpc.discovery.nacos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.forcelorda.rpc.constants.NacosConstant;
import com.forcelorda.rpc.discovery.ServiceDiscovery;
import com.forcelorda.rpc.entity.RpcServiceURL;
import com.forcelorda.rpc.loadbalance.LoadBalancer;

public class NacosServiceDiscovery implements ServiceDiscovery {

	ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	ReadLock readLock = readWriteLock.readLock();

	WriteLock writeLock = readWriteLock.writeLock();

	private NamingService naming;

	private LoadBalancer loadBalancer;

	private String serviceName;

	private volatile List<RpcServiceURL> urls = new ArrayList<>();

	public NacosServiceDiscovery(LoadBalancer loadBalancer, String serviceName) {
		this.loadBalancer = loadBalancer;
		this.serviceName = serviceName;
	}

	public void init() {
		try {
			this.naming = NamingFactory.createNamingService(System.getProperty(NacosConstant.NACOS_SERVER_ADDRESS));
			discoverServices();
			subscribe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void subscribe() throws Exception {
		naming.subscribe(serviceName, event -> {
			if (event instanceof NamingEvent) {
				NamingEvent namingEvent = (NamingEvent) event;
				String serverServiceName = namingEvent.getServiceName();
				if (serverServiceName.equals(serviceName)) {
					try {
						writeLock.lock();
						updateServices(namingEvent);
					} finally {
						writeLock.unlock();
					}

				}
			}
		});
	}

	private void updateServices(NamingEvent namingEvent) {
		List<Instance> instances = namingEvent.getInstances();
		for (Instance instance : instances) {
			RpcServiceURL url = new RpcServiceURL();
			url.setServiceName(instance.getServiceName());
			url.setIp(instance.getIp());
			url.setPort(instance.getPort());
			urls.add(url);
		}
	}

	private List<RpcServiceURL> discoverServices() throws Exception {
		List<RpcServiceURL> urls = new ArrayList<>();
		List<Instance> instances = naming.selectInstances(serviceName, true);
		for (Instance instance : instances) {
			RpcServiceURL url = new RpcServiceURL();
			url.setServiceName(instance.getServiceName());
			url.setIp(instance.getIp());
			url.setPort(instance.getPort());
			urls.add(url);
		}
		this.urls = urls;
		return urls;
	}

	@Override
	public RpcServiceURL getService() {
		try {
			readLock.lock();
			return loadBalancer.getRpcServiceURL(urls);
		} finally {
			readLock.unlock();
		}
	}

}
