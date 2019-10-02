package com.lemon.interf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

public class HelloWorld {
 /**
 * 以post方式访问接口
 */
@Test(enabled=true)
  public void doPost() {
	  //http://119.23.241.154:8080/futureloan/mvc/api/member/login
	  //mobilephone=13517315120&pwd=123456
	  //http请求
		HttpPost httpPost=new HttpPost();
		try {
			httpPost.setURI(new URI("http://119.23.241.154:8080/futureloan/mvc/api/member/login"));
			//以表单形式构建参数名-参数值键值对
			NameValuePair mobilephonePair=new BasicNameValuePair("mobilephone", "13517315120") ;
			NameValuePair pwdPair=new BasicNameValuePair("pwd", "123456");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(mobilephonePair);
			params.add(pwdPair);
			params.add(pwdPair);
			//将参数封装在请求体当中
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			//创建客户端对象
			CloseableHttpClient httpClient=HttpClients.createDefault();
			//发送接口请求,获取接口响应
			CloseableHttpResponse httpResponse=httpClient.execute(httpPost);
			//将json格式报文转化为json格式字符串
			String result=EntityUtils.toString(httpResponse.getEntity());
			//输出到控制台
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   		
  }
	/**以get方式访问接口
	 * 
	 */
@Test(enabled=false)
	public void doGet(){
		String url="http://119.23.241.154:8080/futureloan/mvc/api/member/login?mobilephone=13517315120&pwd=123456";
		HttpGet httpGet=new HttpGet(url);
		//创建客户端
		CloseableHttpClient httpClient=HttpClients.createDefault();
		//发送请求
		try {
			CloseableHttpResponse httpResponse=httpClient.execute(httpGet);
			String result=EntityUtils.toString(httpResponse.getEntity());
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
