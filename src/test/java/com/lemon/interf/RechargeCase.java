package com.lemon.interf;

import org.testng.annotations.DataProvider;

import com.lemon.Utils.CaseUtil;
import com.lemon.pojo.Case;

public class RechargeCase extends BaseProcessor{
	@DataProvider
	public Object[][]datas(){
	Object [][] datas=CaseUtil.datas("3", cellNames);
	return datas;
	}
}
