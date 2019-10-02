package com.lemon.interf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

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
import org.apache.xmlbeans.impl.xb.xsdschema.impl.PublicImpl;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.lemon.Utils.ExcelUtil;
import com.lemon.Utils.RestUtil;

public class RegisterCaseV2 {
	@Test(dataProvider="datas")
	public void test(String url,String type,String param){
		//接口调用
		RestUtil.process(url, type, param);
	}

	@DataProvider(name="datas")
	public Object[][] datas(){
		String []cellNames={"Url(接口地址)","Type(接口提交类型)","Params(参数)"};
		Object [][]datas=ExcelUtil.read2("用例", "src/test/resources/cases_v2.xls", cellNames);
		return datas;
	}
}
