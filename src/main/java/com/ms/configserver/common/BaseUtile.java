/**
 * 
 */
package com.ms.configserver.common;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author yangkunguo
 *
 */
@Component
public class BaseUtile {

	@Resource
	ConfigInfoBean configInfo;
	
	
	/**验证配置是否正确，并返回本地配置目录
	 * @return
	 */
	public String validate() {
		String dir=null;
		if(StringUtils.isBlank(configInfo.getSvnBasedir()) && StringUtils.isBlank(configInfo.getGitBasedir())) {
			System.out.println("请配置ConfigServer本地缓存目录：spring.cloud.config.server.git.basedir");
			throw new RuntimeException("请配置ConfigServer本地缓存目录：spring.cloud.config.server.git.basedir");
		}
		//先配置一个目录
		//Git
		if(StringUtils.isBlank(configInfo.getActive()) || configInfo.getActive().equals("git")) {
			
			dir=configInfo.getGitBasedir();
			if(configInfo.getGitSearchPaths()!=null) {
				dir+="/"+configInfo.getGitSearchPaths()[0];
			}
		//SVN
		}else if(StringUtils.isNotBlank(configInfo.getActive()) && configInfo.getActive().equals("subversion")) {
			
			dir=configInfo.getSvnBasedir();
			if(configInfo.getSvnSearchPaths()!=null) {
				dir+="/"+configInfo.getSvnSearchPaths()[0];
			}
		}
		//native 不支持
		return dir;
	}
	
	/**验证配置是否正确，并返回本地配置根目录
	 * @return
	 */
	public String getBaseDir() {
		String dir=null;
		if(StringUtils.isBlank(configInfo.getSvnBasedir()) && StringUtils.isBlank(configInfo.getGitBasedir())) {
			System.out.println("请配置ConfigServer本地缓存目录：spring.cloud.config.server.git.basedir");
			throw new RuntimeException("请配置ConfigServer本地缓存目录：spring.cloud.config.server.git.basedir");
		}
		//先配置一个目录
		//Git
		if(StringUtils.isBlank(configInfo.getActive()) || configInfo.getActive().equals("git")) {
			dir=configInfo.getGitBasedir();
		//SVN
		}else if(StringUtils.isNotBlank(configInfo.getActive()) && configInfo.getActive().equals("subversion")) {
			dir=configInfo.getSvnBasedir();
		}
		//native 不支持
		return dir;
	}
}
