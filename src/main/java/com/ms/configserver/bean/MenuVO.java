package com.ms.configserver.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangkunguo
 *
 */
public class MenuVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String project;
	private List<Env> envs=new ArrayList<>();
	
	
	public class Env{
		
		private String env;
		private String fileName;
		
		public String getEnv() {
			return env;
		}
		public void setEnv(String env) {
			this.env = env;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		
	}

	public Env instansEnv() {
		return new Env();
	}

	public String getProject() {
		return project;
	}


	public void setProject(String project) {
		this.project = project;
	}


	public List<Env> getEnvs() {
		return envs;
	}


	public void setEnvs(List<Env> envs) {
		this.envs = envs;
	}
}
