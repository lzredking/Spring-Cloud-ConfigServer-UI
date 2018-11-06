/**
 * 
 */
package com.ms.configserver.compoment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ms.configserver.endpoint.ResBean;

/**
 * @author yangkunguo
 *
 */
@Component
public class LocalResources {

	
	/**返回所有文件和文件里的信息
	 * @param dir
	 * @return
	 */
	public Map<String, Map<String,String>> loadResources(String dir) {

		//cache有直接返回
		if(ResourceCache.containsPro(dir)) {
			return ResourceCache.getPro(dir);
		}
		Map<String, Map<String,String>> fileMaps=new HashMap<>();
		Map<String,String> envs=null;
		File file=new File(dir);
		if(file.isDirectory()) {
			String files[]=file.list();
			for(String name:files) {
				System.out.println("LocalResources.class "+name);
				int index=name.lastIndexOf("-");
				String project=name.substring(0, index);
				String env=name.substring(index+1,name.indexOf("."));
				//
				if(fileMaps.containsKey(project)) {
					envs=fileMaps.get(project);
				}else {
					envs=new HashMap<>();
				}
				envs.put(env, name);
				fileMaps.put(project, envs);
			}
			
			ResourceCache.updatePro(dir, fileMaps);
			
		}
		return fileMaps;
	}
}
