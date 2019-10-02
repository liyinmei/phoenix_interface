package com.lemon.interf;

import org.testng.annotations.DataProvider;

import com.lemon.Utils.CaseUtil;

public class GetMemberList extends BaseProcessor{
	@DataProvider(name="datas")
	public Object[][] datas(){
		Object [][] datas=CaseUtil.datas("5",  cellNames);
		return datas;
	}
}
