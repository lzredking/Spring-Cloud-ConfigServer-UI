/**
 * 
 */
package com.ms.configserver.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ms.configserver.bean.MenuVO;
import com.ms.configserver.bean.MenuVO.Env;
import com.ms.configserver.common.BaseUtile;
import com.ms.configserver.common.ConfigInfoBean;
import com.ms.configserver.compoment.LocalResources;

/**
 * @author yangkunguo
 *
 */
@Controller
public class UIController {

	@Resource
	BaseUtile baseUtile;
	
	@Resource
	ConfigInfoBean configInfo;
	
	@Resource
	LocalResources localResources;
	
	/**
	 * @return
	 */
	@RequestMapping("/login")
	public String index() {
		
		return "/html/index";
	}
	
	/**主界面
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/main",method= {RequestMethod.POST})
	public String main(Map <String, Object> map,
			@RequestParam("user") String user,
			@RequestParam("password") String password,HttpServletRequest req) {
		//简单验证
//		if(req.getMethod().equals("get")) {
//			map.put("msg", "用户密码不能为空！");
//			return "/html/index";
//		}
		if(StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
			map.put("msg", "用户密码不能为空！");
			return "/html/index";
		}else {
			if(user.equals(configInfo.getGitUser())
					&& password.equals(configInfo.getGitPwd())) {
				
			}else {
				map.put("msg", "用户或密码不正确，请使用Git帐号登录！");
				//return "/html/index";
			}
		}
		String dir=baseUtile.validate();
		Map<String, Map<String, String>> maps = localResources.loadResources(dir);
		List<MenuVO> menus=new ArrayList<>();
		List<Env> envs=null;
		MenuVO menu=null;
		Env env=null;
		
		for(Entry<String, Map<String, String>> kv: maps.entrySet()) {
			menu=new MenuVO();
			menu.setProject(kv.getKey());
			envs=new ArrayList<>();
			for(Entry<String, String> skv: kv.getValue().entrySet()) {
				env=menu.instansEnv();
				env.setEnv(skv.getKey());
				env.setFileName(skv.getValue());
				
				envs.add(env);
			}
			menu.setEnvs(envs);
			menus.add(menu);
		}
				
//		request.setAttribute("menu", menus);
		map.put("menu", menus);
		
		return "/html/main";
	}
	
	
	/**返回配置信息页
	 * @param map
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/initCentent/{fileName}")
	public String initMenu(Map <String, Object> map,@PathVariable String fileName) {
		map.put("fileName", fileName);
		return "/html/centent";
	}
	
	@RequestMapping("/addRes")
	public String addRes(Map <String, Object> map) {
		
		return "/html/add_res";
	}
	
	/**重新Pull取文件
	 * @param map
	 * @return
	 */
	@RequestMapping("/clone")
	public String test(Map <String, Object> map) {
		
		return "/html/del";
	}
	
	
}
