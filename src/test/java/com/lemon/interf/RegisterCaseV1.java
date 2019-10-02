package com.lemon.interf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

import com.lemon.Utils.ExcelUtil;

public class RegisterCaseV1 {
	@Test(dataProvider="datas")
	public void test(String url,String type,String mobilephone,String pwd){
		if ("post".equalsIgnoreCase(type)) {
			try {
				HttpPost httpPost=new HttpPost(url);
				NameValuePair mobilephonePair=new BasicNameValuePair("mobilephone",mobilephone);
				NameValuePair pwdPair=new BasicNameValuePair("pwd", pwd);
				List<NameValuePair> param=new ArrayList<NameValuePair>();
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
			} catch(Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	@DataProvider
	public Object [][] datas(){
		String [] cellNames={"Url(接口地址)","Type(接口提交类型)","Mobilephone(手机号)","Pwd(密码)"};
		Object[][] datas=ExcelUtil.read2("用例", "src/test/resources/cases_v1.xls", cellNames);
		return datas;
	}
}
