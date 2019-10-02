package com.lemon.interf;

import org.testng.annotations.DataProvider;

import com.lemon.Utils.CaseUtil;

public class Withdraw extends BaseProcessor {
	@DataProvider(name="datas")
	public Object[][] datas(){
		Object [][] datas=CaseUtil.datas("4",  cellNames);
		return datas;
	}
}
