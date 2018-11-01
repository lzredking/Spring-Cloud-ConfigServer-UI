/**
 * 
 */
package com.ms.configserver.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yangkunguo
 *
 */
@Component
public class ConfigInfoBean {

	@Value("${spring.cloud.config.server.svn.basedir:}")
	private String svnBasedir;
	@Value("${spring.cloud.config.server.git.basedir:}")
	private String gitBasedir;
	
	@Value("${spring.cloud.config.server.svn.search-paths:config-server}")
	private String[] svnSearchPaths;
	
	@Value("${spring.cloud.config.server.git.search-paths}")
	private String[] gitSearchPaths;
	
	@Value("${spring.cloud.config.server.git.username:}")
	private String gitUser;
	
	@Value("${spring.cloud.config.server.git.password:}")
	private String gitPwd;
	
	@Value("${spring.cloud.config.server.git.uri:}")
	private String gitPath;
	
	@Value("${spring.profiles.active:}")
	private String active;
	//profiles.active: subversion

	public String getSvnBasedir() {
		return svnBasedir;
	}

	public void setSvnBasedir(String svnBasedir) {
		this.svnBasedir = svnBasedir;
	}

	public String getGitBasedir() {
		return gitBasedir;
	}

	public void setGitBasedir(String gitBasedir) {
		this.gitBasedir = gitBasedir;
	}

	public String[] getSvnSearchPaths() {
		return svnSearchPaths;
	}

	public void setSvnSearchPaths(String[] svnSearchPaths) {
		this.svnSearchPaths = svnSearchPaths;
	}

	public String[] getGitSearchPaths() {
		return gitSearchPaths;
	}

	public void setGitSearchPaths(String[] gitSearchPaths) {
		this.gitSearchPaths = gitSearchPaths;
	}

	public String getGitUser() {
		return gitUser;
	}

	public void setGitUser(String gitUser) {
		this.gitUser = gitUser;
	}

	public String getGitPwd() {
		return gitPwd;
	}

	public void setGitPwd(String gitPwd) {
		this.gitPwd = gitPwd;
	}

	public String getGitPath() {
		return gitPath;
	}

	public void setGitPath(String gitPath) {
		this.gitPath = gitPath;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
	
}
