package com.ms.configserver.endpoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangkunguo
 *
 */
public class ResBean<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	private String msg;
	
	private T obj;
	
	public ResBean(T obj) {
		super();
		this.obj = obj;
	}
	
	public ResBean() {
		super();
	}
	public ResBean(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		if(code==null)code=200;
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
	
	
}
