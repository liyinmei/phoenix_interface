package com.lemon.interf;

import org.apache.http.util.CharsetUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lemon.Utils.CaseUtil;
import com.lemon.Utils.ExcelUtil;
import com.lemon.Utils.RestUtil;
import com.lemon.pojo.Rest;

public class RegisterCaseV3 {
	@Test(dataProvider="datas")
	public void test(String caseId,String apiId,String param){
		String url=RestUtil.getUrlByApiId(apiId);
		String type=RestUtil.getTypeByApiId(apiId);
		RestUtil.process(url, type, param);
	}
	
	@DataProvider(name="datas")
	public Object[][] datas(){
		String [] cellNames={"CaseId(用例编号)","ApiId(接口编号)","Params(参数)"};
		Object [][] datas=ExcelUtil.read2("用例", "src/test/resources/cases_v3.xlsx", cellNames);
		return datas;
	}
}
