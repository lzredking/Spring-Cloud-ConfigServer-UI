package com.ms.configserver.endpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

import com.ms.configserver.bean.ResVO;
import com.ms.configserver.common.BaseUtile;
import com.ms.configserver.common.ConfigInfoBean;
import com.ms.configserver.compoment.LocalResources;
import com.ms.configserver.compoment.LockCache;
import com.ms.configserver.compoment.ResFileUtil;
import com.ms.configserver.compoment.ResourceCache;
import com.ms.configserver.jgit.JGitUtil;

@RestController
public class ResourceEndpoint {

	@Resource
	ConfigInfoBean configInfo;
	
	@Resource
	BaseUtile baseUtile;
	
	@Resource
	LocalResources localResources;
	
	/**添加和修改，不提交
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST},value = "/addkv/{fileName}/{key}/{value}")
	public ResBean addResKey(@PathVariable String fileName,@PathVariable String key,@PathVariable String value) {
		
		if(StringUtils.isBlank(fileName) || StringUtils.isBlank(key)) {
			return new ResBean(400,"文件名不能为空");
		}
		//检查锁
		if(LockCache.isLock(fileName+"-"+key)) {
			return new ResBean(401, "此文件资源已经占用，请稍候再试！");
		}
		LockCache.lock(fileName,"");
		LockCache.lock(fileName+"-"+key,"");
		String exception=null;
		try {
			String dir=baseUtile.validate();
			Map<String,String> cache=new HashMap<String,String>();
			
//			cache=ResFileUtil.loadFiel(dir+"/"+fileName);
			cache=ResFileUtil.writerFiel(dir+"/"+fileName, key, value);

			ResourceCache.updateResource(dir+"/"+fileName, cache);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			LockCache.unLock(fileName+"-"+key);
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	/**提交文件,此时提交无法锁定，可能会覆盖别人提交的内容
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST},value = "/pushFile/{fileName}")
	public ResBean pushFile(@PathVariable String fileName) {
		
		if(StringUtils.isBlank(fileName)) {
			return new ResBean(400,"文件名不能为空");
		}
		//检查锁
//		if(LockCache.isLock(fileName)) {
//			return new ResBean(401, "此文件资源已经占用，请稍候再试！");
//		}
//		LockCache.lock(fileName,"");
		
		String exception=null;
		try {
			
			
			String res=JGitUtil.gitCommitAndPush(new File(baseUtile.getBaseDir()),configInfo.getGitUser(),configInfo.getGitPwd());
			if(StringUtils.isBlank(res)) {
				return new ResBean(401, "push 失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			LockCache.unLock(fileName);
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	/**添加和修改，并提交文件
	 * @param key
	 * @param value
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST},value = "/add/{fileName}/{key}/{value}")
	public ResBean addResKeyAndPush(@PathVariable String fileName,@PathVariable String key,@PathVariable String value) {
		
		if(StringUtils.isBlank(fileName) || StringUtils.isBlank(key)) {
			return new ResBean(400,"文件名不能为空");
		}
		//检查锁
		if(LockCache.isLock(fileName+"-"+key)) {
			return new ResBean(401, "此文件资源已经占用，请稍候再试！");
		}
		LockCache.lock(fileName+"-"+key,"");
		String exception=null;
		try {
			String dir=baseUtile.validate();
			

			Map<String,String> cache=ResFileUtil.writerFiel(dir+"/"+fileName, key, value);
			
			ResourceCache.updateResource(dir+"/"+fileName, cache);
			
			String res=JGitUtil.gitCommitAndPush(new File(baseUtile.getBaseDir()),configInfo.getGitUser(),configInfo.getGitPwd());
			if(StringUtils.isBlank(res)) {
				return new ResBean(401, "push 失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			LockCache.unLock(fileName+"-"+key);
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	/**
	 * @param fileName
	 * @param key
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST},value = "/remove/{fileName}/{key}")
	@ResponseBody
	public ResBean removeResKeyAndPush(@PathVariable String fileName,@PathVariable String key) {
		
		if(StringUtils.isBlank(fileName) || StringUtils.isBlank(key)) {
			return new ResBean(400,"文件名不能为空");
		}
		//检查锁
		if(LockCache.isLock(fileName+"-"+key)) {
			return new ResBean(401, "此文件资源已经占用，请稍候再试！");
		}
		LockCache.lock(fileName+"-"+key,key);
		String exception=null;
		try {
			String dir=baseUtile.validate();
			
			Map<String,String> cache=ResFileUtil.removeKey(dir+"/"+fileName, key);
			
//			Properties pro=new Properties();
//			File file=new File(dir+"/"+fileName);
//			InputStream in =new FileInputStream(file);
//			pro.load(in);
//			in.close();
//			//删除Key
//			FileOutputStream out = new FileOutputStream(file,false);//true表示追加打开
//			pro.remove(key);
//			pro.store(out, "del key : "+key+" = "+ResourceCache.getPro(dir+"/"+fileName).get(key));
//			out.close();

			//删除Cache
			ResourceCache.delResKey(dir+"/"+fileName, key);
			//Git提交
			String res=JGitUtil.gitCommitAndPush(new File(baseUtile.getBaseDir()),configInfo.getGitUser(),configInfo.getGitPwd());
			if(StringUtils.isBlank(res)) {
				return new ResBean(401, "push 失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			LockCache.unLock(fileName+"-"+key);
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	/**加载文件内容
	 * @param fileName
	 * @param key
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST,RequestMethod.GET},value = "/load/{fileName}")
	public ResBean loadRes(@PathVariable String fileName) {
		
		if(StringUtils.isBlank(fileName) ) {
			return new ResBean(400,"文件名不能为空");
		}
		//检查锁
		
		String exception=null;
		try {
			String dir=baseUtile.validate();
			Map<String,String> cache=new HashMap<String,String>();
			//cache有直接返回
			if(ResourceCache.containsResource(fileName)) {
				cache= ResourceCache.getResource(fileName);
			}else {
				
				//
				cache=ResFileUtil.loadFiel(dir+"/"+fileName);
				ResourceCache.updateResource(dir+"/"+fileName, cache);
			}
			ResVO vo=null;
			List<ResVO> vos=new ArrayList<>();
			
			for(Entry<String, String> kv:cache.entrySet()) {
				vo=new ResVO(kv.getKey(), kv.getValue());
				vos.add(vo);
			}
			return new ResBean(vos);
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	/**
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST,RequestMethod.GET},value = "/loadReses")
	public ResBean loadResources() {

		String exception=null;
		try {
			String dir=baseUtile.validate();
			
			File file=new File(dir);
			if(file.isDirectory()) {
				
				Map<String, Map<String,String>> fileMaps=localResources.loadResources(dir);
				return new ResBean(fileMaps);
			}else {
				return new ResBean(401, "配置的目录不正确");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	/**Clone文件
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST,RequestMethod.GET},value = "/loadResFile")
	public ResBean loadResFile() {

		String exception=null;
		try {
			String dir=baseUtile.getBaseDir();
			
			File file=new File(dir);
			System.out.println(file.getParentFile().getPath());
			ResFileUtil.delFile(file.getParentFile());
			
			//Git
			String msg=JGitUtil.gitClone(configInfo.getGitPath(), new File(baseUtile.getBaseDir()));
			
			//JGitUtil.gitFetch(new File(getBaseDir()),gitUser,gitPwd);
			ResourceCache.clean();
			if(msg!=null) {
				return new ResBean(401, "error msg:"+msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean(200,"强制同步服务器配置文件成功！");
	}
	
	/**新增配置文件
	 * @param resName
	 * @return
	 */
	@RequestMapping(method ={RequestMethod.POST},value = "/addResFile/{resName}")
	public ResBean addResFile(@PathVariable String resName) {

		String exception=null;
		try {
			String dir=baseUtile.validate();
			
			File file=new File(dir+"/"+resName+".properties");
			System.out.println(file.getPath());
			if(!file.isFile()) {
				file.createNewFile();
			}
			//Git,此时不用提交。还是空文件，在添加属性的时候直接提交
//			String res=JGitUtil.gitCommitAndPush(new File(baseUtile.getBaseDir()),configInfo.getGitUser(),configInfo.getGitPwd());
//			if(StringUtils.isBlank(res)) {
//				return new ResBean(401, "push 失败");
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			exception=e.getMessage();
		}finally {
			if(StringUtils.isNotBlank(exception)) {
				return new ResBean(401, "error msg: "+exception);
			}
		}
		return new ResBean();
	}
	
	
}
