package com.lemon.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.DBChecker;
import com.lemon.pojo.DBCheckerResult;

/**数据库验证工具类
 * @author lym
 *
 */
public class DBCheckerUtil {
	/**数据验证
	 * @param paramSql  要传入的满足json数组格式的字符串（包含查询SQL）
	 * @return
	 */
	public static String validate(String paramSql) {
		List<DBChecker> dbCheckersList = new ArrayList<DBChecker>();
	    List<DBCheckerResult> dbCheckerResultList = new ArrayList<DBCheckerResult>();
		//将json数组格式的字符串转化为list集合
		dbCheckersList = JSONObject.parseArray(paramSql, DBChecker.class);
		for (DBChecker dbChecker : dbCheckersList) {
			String no=dbChecker.getNo();
			String sql = dbChecker.getSql();
			try {
				//执行sql
				Map<String, Object>	columnLablelAndValue = JDBCUtil.query(sql, null);
				DBCheckerResult dbCheckerResult =new DBCheckerResult();
				//将SQL执行后的结果封装为对象
				dbCheckerResult.setNo(no);
				dbCheckerResult.setColumnLablelAndValue(columnLablelAndValue);
				//将对象添加到集合中
				dbCheckerResultList.add(dbCheckerResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//将集合转化为json数组格式的字符串
		String result = JSONObject.toJSONString(dbCheckerResultList);
		return result;
	}

}
