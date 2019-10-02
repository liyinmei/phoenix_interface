package com.lemon.interf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterCaseV0 {
	@Test(dataProvider="datas")
	public void test(String url,String type,String mobilephone,String pwd ){
		if ("post".equalsIgnoreCase(type)) {
			try {
				HttpPost httpPost=new HttpPost(url);
				NameValuePair mobilephonePair=new BasicNameValuePair("mobilephone", mobilephone);
				NameValuePair pwdPair=new BasicNameValuePair("pwd", pwd);
				List<NameValuePair>  param=new ArrayList<NameValuePair>();
				param.add(mobilephonePair);
				param.add(pwdPair);
				httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
				CloseableHttpClient httpClient=HttpClients.createDefault();
				CloseableHttpResponse httpResponse=httpClient.execute(httpPost);
				String result=EntityUtils.toString(httpResponse.getEntity());
				System.out.println(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}else if ("get".equalsIgnoreCase(type)) {
			url+="?mobilephone="+mobilephone+"&pwd="+pwd;
			HttpGet httpGet=new HttpGet(url);
			CloseableHttpClient httpClient=HttpClients.createDefault();
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
	
	@DataProvider
	public Object [][]datas(){
		Object [][] datas={
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","19000000000",""},
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","","123456"},
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","123","123456"},
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","19000000000","12345"},
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","18000000000","123456"},
				{"http://119.23.241.154:8080/futureloan/mvc/api/member/register","post","18000000000","123456"},
		};
		return datas;
	}
}
