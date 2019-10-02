package com.lemon.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.Rest;

/**接口调用工具类
 * @author lym
 *
 */
public class RestUtil {
	
	public static List<Rest> rests=new ArrayList<Rest>();
	public static Map<String, String> addAuthenticInfoMap=new HashMap<String, String>();
	static{
		ExcelUtil.loadDatas(PropertiesUtil.getCaseFilePath(),"接口信息",Rest.class);
	}
	public static String process(String url, String type, String param) {
		String result="";
		//解析json格式的字符串数据并保存到map中
		Map<String, String> paramMap=(Map<String, String>) JSONObject.parse(param);
		if ("post".equalsIgnoreCase(type)) {
			result=doPost(url,paramMap);
		}else if ("get".equalsIgnoreCase(type)) {
			result=doGet(url,paramMap);
			
		}
		return result;
	}

	/**以get方式提交
	 * @param url 接口地址
	 * @param paramMap 保存json格式字符串的map
	 */
	private static String doGet(String url, Map<String, String> paramMap) {
		String result="";
		List<NameValuePair> paramList=new ArrayList<NameValuePair>();
		Set<Entry<String, String>> entries=paramMap.entrySet();
		for (Entry<String, String> entry : entries) {
			//以表单形式构建参数名-参数值键值对
			NameValuePair pair=new BasicNameValuePair(entry.getKey(),entry.getValue());
			paramList.add(pair);
		}
		//拼接参数，得到接口地址
		url+="?"+URLEncodedUtils.format(paramList, "UTF-8");
		HttpGet httpGet=new HttpGet(url);
		//创建客户端对象
		CloseableHttpClient httpClient=HttpClients.createDefault();
		try {
			//添加鉴权头信息
			addAuthenticInfo(httpGet);
			//发送请求，获取接口响应
			CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
			//保存头信息
			saveAuthenticInfo(httpResponse);
			//将接口响应转换为json格式的字符串
		    result=EntityUtils.toString(httpResponse.getEntity());
			 System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}

	private static String doPost(String url,Map<String, String> paramMap) {
		String result="";
		HttpPost httpPost=new HttpPost(url);
		List<NameValuePair> paramList=new ArrayList<NameValuePair>();
		try {
			Set<Entry<String, String>> entries=paramMap.entrySet();
			for (Entry<String, String> entry : entries) {
				//以表单形式构建参数名-参数值键值对
				NameValuePair pair=new BasicNameValuePair(entry.getKey(),entry.getValue());
				paramList.add(pair);
			}
			//将参数封装在请求体当中
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			//创建客户端对象
			CloseableHttpClient httpClient=HttpClients.createDefault();
			//添加鉴权头信息
			addAuthenticInfo(httpPost);
			//发送请求，获取接口响应
			CloseableHttpResponse httpResponse=httpClient.execute(httpPost);
			//将鉴权的头信息取出来，并保存
			saveAuthenticInfo(httpResponse);
			//将json格式报文转换为json格式的字符串
			result=EntityUtils.toString(httpResponse.getEntity());
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**保存鉴权头信息
	 * @param httpResponse
	 */
	private static void saveAuthenticInfo(CloseableHttpResponse httpResponse) {
		//通过响应拿到响应头中Set-Cookie信息
		Header header=httpResponse.getFirstHeader("Set-Cookie");
		if (header!=null) {
			//拿到header中set-cookie的值
			String value=header.getValue();
			//判断值中是否有JSESSIONID，没有返回-1
			int index=value.indexOf("JSESSIONID");
			if (value!=null&&index!=-1) {
				//截取JSESSIONID的值
				String authenticInfo=value.substring(index,value.indexOf(";"));
				addAuthenticInfoMap.put("jsessionid", authenticInfo);
			}
			
		}
	}

	/**添加鉴权头信息
	 * @param httpRequest
	 */
	private static void addAuthenticInfo(HttpRequest httpRequest) {
		String value =addAuthenticInfoMap.get("jsessionid");
		//如果map中有jsessionid的值且不为空，在请求头加上头信息
		if (value!=null&&value.trim().length()>0) {
			
			httpRequest.addHeader("Cookie", value);
		}
	}

	/**根据接口编号获取接口地址
	 * @param apiId
	 * @return
	 */
	public static String getUrlByApiId(String apiId) {
		for (Rest rest : rests) {
			if (rest.getApiId().equals(apiId)) {
				return rest.getUrl();
			}
		}
		return null;
	}

	/**根据接口编号获取接口类型
	 * @param apiId
	 * @return
	 */
	public static String getTypeByApiId(String apiId) {
		for (Rest rest : rests) {
			if (rest.getApiId().equals(apiId)) {
				return rest.getType();
			}
		}
		return null;
	}
		
}
