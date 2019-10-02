package com.lemon.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**properties解析工具类
 * @author lym
 *
 */
public class PropertiesUtil {
	public static Properties properties =new Properties();
	
	static {
		loadUrls();
	}
	
	/**加载文件路径信息
	 * 
	 */
	private static void loadUrls() {
		InputStream inStream;
		try {
			inStream = new FileInputStream(new File("src/test/resources/properties/url.properties"));
			properties.load(inStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**获取用例文件路径
	 * @return
	 */
	public static String getCaseFilePath(){
		String url = properties.getProperty("case.url");
		return url;
	}
}
