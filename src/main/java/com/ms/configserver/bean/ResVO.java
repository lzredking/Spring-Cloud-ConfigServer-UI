/**
 * 
 */
package com.ms.configserver.bean;

import java.io.Serializable;

/**
 * @author yangkunguo
 *
 */
public class ResVO implements Serializable{

	private String id;
	
	private String key;
	
	private String value;
	
	private String status="p";

	public ResVO(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return key;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
