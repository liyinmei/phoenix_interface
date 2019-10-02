package com.lemon.interf;

import org.apache.http.protocol.ResponseDate;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.lemon.Utils.DBCheckerUtil;
import com.lemon.Utils.ExcelUtil;
import com.lemon.Utils.PropertiesUtil;
import com.lemon.Utils.RestUtil;
import com.lemon.Utils.VariableUtil;
import com.mysql.cj.api.xdevapi.Result;

public class BaseProcessor {
	public  String [] cellNames={"CaseId","ApiId","Params","PreValidateSql","AfterValidateSql"};
	@Test(dataProvider="datas")
	public void test(String caseId,String apiId,String param,String preValidateSql,String afterValidateSql){
		//替换变量值
		param = VariableUtil.replaceParam(param);
		preValidateSql = VariableUtil.replaceParam(preValidateSql);
		afterValidateSql = VariableUtil.replaceParam(afterValidateSql);
		//接口执行前数据验证
		if (preValidateSql!=null&&preValidateSql.trim().length()>0) {
			String preResult =DBCheckerUtil.validate(preValidateSql);
			ExcelUtil.saveWriteBackDatas("用例",caseId, "PreValidateResult", preResult);
		}
		String url=RestUtil.getUrlByApiId(apiId);
		String type=RestUtil.getTypeByApiId(apiId);
		String result=RestUtil.process(url, type, param);
		//ExcelUtil.writeDatas("src/test/resources/cases_v5.xlsx","用例",caseId,"ActualResponseData",result);
		ExcelUtil.saveWriteBackDatas("用例",caseId, "ActualResponseData", result);
		//接口执行后数据验证
		if (afterValidateSql!=null&&afterValidateSql.trim().length()>0) {
			String afterResult =DBCheckerUtil.validate(afterValidateSql);
			ExcelUtil.saveWriteBackDatas("用例",caseId, "AfterValidateResult", afterResult);
		}
	}
	
	@AfterSuite
	public void ending(){
		//批量回写测试数据
		ExcelUtil.batchWriteDatas(PropertiesUtil.getCaseFilePath());
		
	}
}
