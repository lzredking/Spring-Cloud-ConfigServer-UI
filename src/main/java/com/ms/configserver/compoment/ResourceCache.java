/**
 * 
 */
package com.ms.configserver.compoment;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangkunguo
 *
 */
public class ResourceCache {

	/**
	 * 
	 */
	private static Map<String, Map<String,String>> concurrentMap= new ConcurrentHashMap<>();
	
	/**
	 * 资源环境配置信息
	 */
	private static Map<String, Map<String, Map<String,String>>> proMap= new ConcurrentHashMap<>();
	
	
	/**
	 * @param key
	 * @return
	 */
	public static boolean containsPro(String key) {
		return proMap.containsKey(key);
	}
	public static void updatePro(String key,Map<String, Map<String,String>> value) {
		proMap.put(key, value);
	}
	public static Map<String, Map<String,String>> getPro(String key) {
		return proMap.get(key);
	}
	///////////////////////////////////////////////////////////////
	/**
	 * @param fileName
	 * @return
	 */
	public static boolean containsResource(String fileName){
		return concurrentMap.containsKey(fileName);
	}
	
	/**
	 * @param fileName
	 * @return
	 */
	public static Map<String,String> getResource(String fileName){
		return concurrentMap.get(fileName);
	}
	
	/**put and update
	 * @param fileName
	 * @param values
	 */
	public static void updateResource(String fileName,Map<String,String> values){
		concurrentMap.put(fileName,values);
	}
	
	/**
	 * @param fileName
	 */
	public static void delResource(String fileName){
		concurrentMap.remove(fileName);
	}
	
	/**
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void updateResValue(String fileName,String key,String value){
		Map<String,String> map=concurrentMap.get(fileName);
		map.put(key, value);
		
//		for(Entry<String,String> kv: map.entrySet()) {
//		}
	}
	
	/**
	 * @param fileName
	 * @param key
	 */
	public static void delResKey(String fileName,String key){
		Map<String,String> map=concurrentMap.get(fileName);
		map.remove(key);
		
	}
	
	public static void clean() {
		concurrentMap.clear();
		proMap.clear();
	}
}
