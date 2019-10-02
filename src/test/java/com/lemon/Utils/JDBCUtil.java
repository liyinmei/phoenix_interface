package com.lemon.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**数据库查询工具类
 * @author lym
 *
 */
public class JDBCUtil {
	private static Properties properties=new Properties();
	//通过静态代码块将配置文件里的信息一次性加载到内存中
	//静态代码块在类被加载的时候就被加载，最先被执行且只会执行一次，
	static{
		try {
			//获取输入流对象，将数据读到流里去
			InputStream  iStream = new FileInputStream(new File("src/test/resources/properties/jdbc.properties"));
			//将流里的数据加载到properties对象中
			properties.load(iStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param sql 需要查询的脚本
	 * @param values SQL脚本中条件字段的值
	 * @throws Exception 
	 */
	public static Map<String, Object> query(String sql,List<Object> values) throws Exception{
		//提供创建连接所需要的值
		String url=properties.getProperty("jdbc.url");
		String password=properties.getProperty("jdbc.password");
		String username=properties.getProperty("jdbc.username");
		//获取数据库连接
		Connection connection=DriverManager.getConnection(url, username, password);
		//传进去需要执行的SQL查询,获取preparedStatement对象（preparedStatement提供了SQL预编译的机制）
		PreparedStatement pStatement=connection.prepareStatement(sql);
		//替换占位符的值（用占位符是为了预编译SQL，防止SQL注入的漏洞发生）
		if (values!=null) {
			for(int i=1;i<=values.size();i++){
				pStatement.setObject(i, values.get(i-1));
			}
		}
		//执行查询，获取结果集
		ResultSet resultSet=pStatement.executeQuery();
		//获取结果集元数据（从这个对象中拿到查询的信息，比如查询的字段个数，名称）
		ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
		Map<String, Object> labelAndValue=new HashMap<String, Object>();
		//从结果集里获取我们想要的数据
		while(resultSet.next()){
			int size=resultSetMetaData.getColumnCount();//获取结果列的个数
			for(int i=0;i<size;i++){
				//根据列号获取列名		
				String columnLabel=resultSetMetaData.getColumnLabel(i+1);
				Object columnValue=resultSet.getObject(columnLabel);
				labelAndValue.put(columnLabel, columnValue);
			}
			//将结果返回到map
		}
		return labelAndValue;
	}
}
