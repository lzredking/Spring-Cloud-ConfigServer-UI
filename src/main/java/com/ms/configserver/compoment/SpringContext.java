package com.ms.configserver.compoment;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yangkunguo
 *
 */
@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringContext.applicationContext = arg0;
	}

	/**
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return  applicationContext.getBean(name);
	}

	/**
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		return (T) applicationContext.getBeansOfType(clazz);
	}

}
