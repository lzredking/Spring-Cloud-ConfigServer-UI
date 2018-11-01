package com.ms.configserver.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author yangkunguo
 *
 */
@EnableConfigServer
@EnableDiscoveryClient//服务发现与注册
@EnableEurekaClient
@SpringBootApplication(scanBasePackages= {"com.ms.configserver"})
public class ConfigServerApplication {

	public static ConfigurableApplicationContext context;//bena容器
	
	
	public static void main(String[] args) {
		context= SpringApplication.run(ConfigServerApplication.class, args);
	}
  
}