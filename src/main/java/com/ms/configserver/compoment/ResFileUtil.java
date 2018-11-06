/**
 * 
 */
package com.ms.configserver.compoment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.*;





/**perproties yml 文件操作
 * @author yangkunguo
 *
 */
public class ResFileUtil {

	private static String encoding="UTF-8";
	
	private static String tempDir="/9876";
	/**读取配置文件
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> loadFiel(String path){
		
		System.out.println(path);
		Map<String,String> map=new LinkedHashMap<String,String>();
		//文件操作
		File file=new File(path);
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		
		try {
			
			if(suffix.toLowerCase().equals("properties")) {
				Properties pro=new Properties();
				
				InputStream in =new FileInputStream(file);
				InputStreamReader reader=new InputStreamReader(in, "UTF-8");
				pro.load(reader);
				in.close();
				reader.close();
				//
				
				map=new LinkedHashMap<String,String>((Map)pro);
				
			}else if(suffix.toLowerCase().equals("yml") || suffix.toLowerCase().equals("yaml")) {
//				DumperOptions dumperOptions = new DumperOptions();
//	            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//	            dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
//	            dumperOptions.setPrettyFlow(false);
//	            Yaml yaml = new Yaml(dumperOptions);
	
				InputStream in =new FileInputStream(file);
				//map = yaml.loadAs(in, HashMap.class);
				map= yaml2Properties(in);
				
//				for(String str:liens) {
//					System.out.println(str);
//					if(str!=null) {
//						
//					}
//				}
//				Iterable<Object> result = yaml.loadAll(new FileInputStream(file));
//		        for (Object obj : result) {
//		            System.out.println(obj.getClass());
//		            System.out.println(obj);
//		        }
				in.close();
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}
	
	/**写入配置文件
	 * @param path
	 * @param key
	 * @param value
	 * @return
	 */
	public static Map<String, String> writerFiel(String path,String key,String value){
		Map<String,String> map=new LinkedHashMap<String,String>();
		
		File file=new File(path);
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		
		OutputStreamWriter out=null;
		FileWriter writer=null;
		try {
			
			if(suffix.toLowerCase().equals("properties")) {
				Properties pro=new Properties();
				
				InputStream in =new FileInputStream(file);
				InputStreamReader reader=new InputStreamReader(in, "UTF-8");
				pro.load(reader);
				in.close();
				reader.close();
				//
				out=new OutputStreamWriter(new FileOutputStream(file,false), Charset.forName(encoding));
//				out = new FileOutputStream(file,false);//true表示追加打开
				pro.put(key, value);
				pro.store(out, "add key ： "+key);
				
				map=new LinkedHashMap<String,String>((Map)pro);
				
			}else if(suffix.toLowerCase().equals("yml") || suffix.toLowerCase().equals("yaml")) {
				//先生成一个临时文件temp.properties
				String temp=tempDir+"/w"+System.currentTimeMillis()+".properties";
				File dir=new File(tempDir);
				if(!dir.exists()) {
					dir.mkdir();
				}
				
				File tempFile=new File(temp);
				tempFile.createNewFile();
//				file.renameTo(tempFile);
				//将变动的值写入文件
				Properties pro=new Properties();
				
				InputStream in =new FileInputStream(tempFile);
				InputStreamReader reader=new InputStreamReader(in, "UTF-8");
				pro.load(reader);
				in.close();
				reader.close();
				//写Temp文件
				out=new OutputStreamWriter(new FileOutputStream(tempFile,false), Charset.forName(encoding));
//				out = new FileOutputStream(tempFile,false);//true表示追加打开
//				pro.put(key, value);
				map=loadFiel(path);
				map.put(key, value);
				
				for(Entry<String, String> kv:map.entrySet()) {
					pro.put(kv.getKey(), kv.getValue());
				}
				pro.store(out, "add key ： "+key);
				
				//再将pro文件转成yaml格式
				
				properties2Yaml(tempFile, file);
				//删除临时文件
				System.out.println(tempFile.getAbsolutePath());
				System.out.println(tempFile.delete());
				out.close();
				
				delFile(new File(tempDir));
//				Yaml yaml = new Yaml();
//				InputStream in =new FileInputStream(file);
////				map = yaml.loadAs(in, HashMap.class);
//				map= yaml2Properties(in);
//				in.close();
//				
//				Map<String,String> kv=new HashMap<>();
//				kv.put(key, value);
//				
////				yaml.dump(kv,new OutputStreamWriter((new FileOutputStream(file))));
//				
//				writer = new FileWriter(file);
//				writer.write(yaml.dump(kv));
//				writer.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally {
			try {
				
				if(out!=null) {out.close();}
				if(writer!=null) {writer.close();}
				
			} catch (Exception e2) {
				throw new RuntimeException(e2.getMessage());
			}
		}
		return map;
	}
	
	public static Map<String, String> removeKey(String path,String key){
		Map<String,String> map=new LinkedHashMap<String,String>();
		
		File file=new File(path);
		String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		
		OutputStreamWriter out=null;
		FileWriter writer=null;
		try {
			
			if(suffix.toLowerCase().equals("properties")) {
				Properties pro=new Properties();
				
				InputStream in =new FileInputStream(file);
				InputStreamReader reader=new InputStreamReader(in, "UTF-8");
				pro.load(reader);
				in.close();
				reader.close();
				//
				out=new OutputStreamWriter(new FileOutputStream(file,false), Charset.forName(encoding));
//				out = new FileOutputStream(file,false);//true表示追加打开
				pro.remove(key);
				pro.store(out, "remov key : "+key+" = "+ResourceCache.getPro(path).get(key));
				
				map=new LinkedHashMap<String,String>((Map)pro);
				
			}else if(suffix.toLowerCase().equals("yml") || suffix.toLowerCase().equals("yaml")) {
				//先生成一个临时文件temp.properties
				String temp=tempDir+"/d"+System.currentTimeMillis()+".properties";
				File dir=new File(tempDir);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				File tempFile=new File(temp);
				tempFile.createNewFile();
				//file.renameTo(tempFile);
				//将变动的值写入文件
				Properties pro=new Properties();
				
				InputStream in =new FileInputStream(tempFile);
				InputStreamReader reader=new InputStreamReader(in, "UTF-8");
				pro.load(reader);
				in.close();
				reader.close();
				//写Temp文件
				out=new OutputStreamWriter(new FileOutputStream(tempFile,false), Charset.forName(encoding));
//				out = new FileOutputStream(tempFile,false);//true表示追加打开
				map=loadFiel(path);
				
				for(Entry<String, String> kv:map.entrySet()) {
					pro.put(kv.getKey(), kv.getValue());
				}
				pro.remove(key);
				pro.store(out, "romve key ： "+key);
				
				
				//再将pro文件转成yaml格式
				properties2Yaml(tempFile, file);
				
				//删除临时文件
				delFile(new File(tempDir));
				

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally {
			try {
				
				if(out!=null) {out.close();}
				if(writer!=null) {writer.close();}
				
			} catch (Exception e2) {
				throw new RuntimeException(e2.getMessage());
			}
		}
		return map;
	}
	
	/**yaml文件转properties文件
	 * @param in
	 * @return
	 */
	private static Map<String, String> yaml2Properties(InputStream in) {
		final String DOT = ".";
//		List<String> lines = new LinkedList<>();
		Map<String, String> lines=new LinkedHashMap<>();
		try {
		    YAMLFactory yamlFactory = new YAMLFactory();
		    YAMLParser parser = yamlFactory.createParser(
		            new InputStreamReader(in, Charset.forName(encoding)));

		    String key = "";
		    String value = null;
		    JsonToken token = parser.nextToken();
		    while (token != null) {
		        if (JsonToken.START_OBJECT.equals(token)) {
		            // do nothing
		        } else if (JsonToken.FIELD_NAME.equals(token)) {
		            if (key.length() > 0) {
		                key = key + DOT;
		            }
		            key = key + parser.getCurrentName();

		            token = parser.nextToken();
		            if (JsonToken.START_OBJECT.equals(token)) {
		                continue;
		            }
		            value = parser.getText();
//		            lines.add(key + "=" + value);
		            if(key!=null && value!=null)
		            	lines.put(key, value);

		            int dotOffset = key.lastIndexOf(DOT);
		            if (dotOffset > 0) {
		                key = key.substring(0, dotOffset);
		            }
		            value = null;
		        } else if (JsonToken.END_OBJECT.equals(token)) {
		            int dotOffset = key.lastIndexOf(DOT);
		            if (dotOffset > 0) {
		                key = key.substring(0, dotOffset);
		            } else {
		                key = "";
//		                lines.add("");
		            }
		        }
		        token = parser.nextToken();
		    }
		    parser.close();

//		    FileUtils.writeLines(this.properties, this.encoding, lines);
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		return lines;
	}
	
	/**properties文件转yaml文件
	 * @param proFile 源文件
	 * @param ymlFile 目标文件
	 */
	private static void properties2Yaml(File proFile,File ymlFile) {
		JsonParser parser = null;
		JavaPropsFactory factory = new JavaPropsFactory();
		try {
			parser = factory.createParser(
					new InputStreamReader(new FileInputStream(proFile), Charset.forName(encoding)));
			
		    YAMLFactory yamlFactory = new YAMLFactory();
		    YAMLGenerator generator = yamlFactory.createGenerator(
		            new OutputStreamWriter(new FileOutputStream(ymlFile), Charset.forName(encoding)));

		    JsonToken token = parser.nextToken();

		    while (token != null) {
		        if (JsonToken.START_OBJECT.equals(token)) {
		            generator.writeStartObject();
		        } else if (JsonToken.FIELD_NAME.equals(token)) {
		            generator.writeFieldName(parser.getCurrentName());
		        } else if (JsonToken.VALUE_STRING.equals(token)) {
		            generator.writeString(parser.getText());
		        } else if (JsonToken.END_OBJECT.equals(token)) {
		            generator.writeEndObject();
		        }
		        token = parser.nextToken();
		    }
		    parser.close();
		    generator.flush();
		    generator.close();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}
	
	/**删除目录下所有文件，如果文件被引用无法删除
	 * @param file
	 */
	public static void delFile(File file) {
		 
		if(file.isDirectory()){
			File files[]=file.listFiles();
			if(files==null || files.length==0) {
				System.out.println("del file "+file.getPath()+" "+file.delete());
			}else {
				
				for(File f:files) {
					if(f.isDirectory()) {
						delFile(f);
					}else {
						System.out.println("del file "+f.getPath()+" "+f.delete());
					}
				}
			}
		}
		System.out.println("del file "+file.getPath()+" "+file.delete());
	}
}
