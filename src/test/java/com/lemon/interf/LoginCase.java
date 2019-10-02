package com.lemon.interf;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lemon.Utils.CaseUtil;
import com.lemon.Utils.ExcelUtil;
import com.lemon.Utils.RestUtil;

public class LoginCase extends BaseProcessor{
	
	@DataProvider(name="datas")
	public Object[][] datas(){
		Object [][] datas=CaseUtil.datas("2",cellNames);
		return datas;
	}
}
