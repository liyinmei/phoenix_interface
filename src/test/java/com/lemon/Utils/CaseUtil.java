package com.lemon.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.lemon.pojo.Case;

/**用例工具类
 * @author lym
 *
 */
public class CaseUtil {
	public static List<Case> cases=new ArrayList<Case>();
	static{
		ExcelUtil.loadDatas(PropertiesUtil.getCaseFilePath(), "用例",Case.class);
	}
	
	
	/**获取对应接口用例信息
	 * @param apiId 接口编号
	 * @param cellNames 要取的列名
	 * @return
	 */
	public static Object [][] datas(String apiId,String[] cellNames){
		//根据ApiId筛选出满足条件的接口对象信息,添加进集合
		List<Case> satisfied=new ArrayList<Case>();
		for (Case case1 : cases) {
			if (case1.getApiId().equals(apiId)) {
				satisfied.add(case1);
			}
		}
		Object [][] datas=new Object[satisfied.size()][cellNames.length];
		Class clazz=Case.class;
		for (int i = 0; i < satisfied.size(); i++) {
			Case case1=satisfied.get(i);
			for (int j = 0; j < cellNames.length; j++) {
				//拼接方法名
				String methodName="get"+cellNames[j];
				try {
					//通过反射获取方法对象
					Method method = clazz.getMethod(methodName);
					String value=(String) method.invoke(case1);
					datas[i][j]=value;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return datas;
	}

}
