package com.ms.configserver.compoment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class IsCluster {

	@Resource
	private DiscoveryClient discoveryClient;//Eureka服务
	
	@Value("${spring.application.name}")
	private String appName;
	
	public boolean init() {
		
		List<ServiceInstance> instances=discoveryClient.getInstances(appName);
    	if(instances != null && instances.size()>1) {
    		return true;
    	}else {
    		return false;
    	}
	}
}
