package com.lemon.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lemon.pojo.VariableConfigration;

public class VariableUtil {
	public static List<VariableConfigration> variableConfigrations = new ArrayList<VariableConfigration>();
	public static Map<String, String> vNameAndValueMap = new HashMap<String, String>();
	static{
		ExcelUtil.loadDatas(PropertiesUtil.getCaseFilePath(), "变量", VariableConfigration.class);
		ExcelUtil.loadRowNumCellNumMappings(PropertiesUtil.getCaseFilePath(), "变量");
		loadvNameAndValueMap();
	}
	
	/**加载变量名与变量值映射关系map
	 * 
	 */
	private static void loadvNameAndValueMap() {
		for (VariableConfigration variableConfigration : variableConfigrations) {
			String paramName = variableConfigration.getName();
			String paramValue = variableConfigration.getValue();
			if (paramValue==null||paramValue.trim().length()==0) {
				//变量值为空时，需要通过反射来获取变量的值，并添加到map中
				//获取需要反射的类路径
				String classPath = variableConfigration.getReflectClass();
				//获取反射需要的方法名
				String methodName = variableConfigration.getReflectMethod();
				try {
					//通过类的路径得到类的字节码
					Class clazz =Class.forName(classPath);
					//通过字节码创建对象
					Object obj = clazz.newInstance();
					//通过字节码得到反射需要的方法对象
					Method method = clazz.getMethod(methodName);
					paramValue = (String) method.invoke(obj);
					//将值添加到map中
					vNameAndValueMap.put(paramName, paramValue);
					ExcelUtil.saveWriteBackDatas("变量", paramName, "ReflectValue", paramValue);
					System.out.println("变量名=【"+paramName+"】，变量值=【"+paramValue+"】");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				//如果变量值不为空，则直接添加到map中
				vNameAndValueMap.put(paramName, paramValue);
				System.out.println("变量名=【"+paramName+"】，变量值=【"+paramValue+"】");
			}
		}
	}

	/**替换变量
	 * @param value
	 * @return
	 */
	public static String replaceParam(String value) {
		Set<String> variables = vNameAndValueMap.keySet();
		for (String varible : variables) {
			//判断如果传进来的字符串包含了当前变量名，那么替换
			if (value.contains(varible)) {
				value = value.replace(varible, vNameAndValueMap.get(varible));
			}
		}
		return value;
	}
	
}
