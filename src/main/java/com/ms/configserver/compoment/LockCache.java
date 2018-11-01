package com.ms.configserver.compoment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ms.configserver.main.ConfigServerApplication;

/**单机是用本地锁，集群时用分布式锁
 * @author yangkunguo
 *
 */
@Component
public class LockCache {

	/**
	 * 
	 */
	private static Map<String, String> concurrentMap= new ConcurrentHashMap<String, String>();
	
	/**
	 * 记录加锁时间
	 */
	private static Map<String, Long> map= new HashMap<>();
	/**
	 * 集群为True,单机时为False
	 */
	private static Boolean cluster=false;
	
	
	@Value("${spring.application.name}")
	private String slefName;
	
	
	private LockCache() {}
	/**
	 * 
	 */
	private static void init() {
		IsCluster isCluster=(IsCluster) SpringContext.getBean("isCluster");
		cluster=isCluster.init();
		
	}
	/** 锁定资源
	 * @param key
	 * @param value
	 * @return 成功返回true 失败返回false
	 */
	public static boolean lock(String key,String value) {

		
		return cacheLock(key,value);
	}
	
	
	/** 取消锁资源
	 * @param key
	 * @return 成功返回true
	 */
	public static boolean unLock(String key) {
		return cacheUnLock( key);
	}
	
	/** 检查资源锁
	 * @param key
	 * @return 已经锁返回true，没锁返回false
	 */
	public static boolean isLock(String key) {
		return cacheIsLock(key);
	}
	
	/**根据环境计算
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean cacheLock(String key,String value) {
		init();
		if(cluster) {
			//.........Redis lock
			map.put(key, System.currentTimeMillis());
		}else {
			if(concurrentMap.containsKey(key))
				return false;
			concurrentMap.put(key, value);
			//加锁时间
			map.put(key, System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	/**根据环境计算
	 * @param key
	 * @return
	 */
	private static boolean cacheUnLock(String key) {
		init();
		if(cluster) {
			//.........Redis lock
			
		}else {
			concurrentMap.remove(key);
			return true;
		}
		return false;
	}
	
	/**根据环境计算
	 * @param key
	 * @return
	 */
	private static boolean cacheIsLock(String key) {
		//
		Long oldTime=map.get(key);
		if(oldTime!=null) {
			long btime=System.currentTimeMillis()-oldTime;
			//5分钟超时 自动解锁
			if(btime/1000/60>5) {
				unLock(key);
			}
		}
		init();
		if(cluster) {
			//.........Redis lock
		}else {
			return concurrentMap.containsKey(key);
		}
		return false;
	}
	public static void setCluster(Boolean cluster) {
		LockCache.cluster = cluster;
	}
	
}
