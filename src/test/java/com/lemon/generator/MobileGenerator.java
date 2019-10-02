package com.lemon.generator;

import java.util.Map;

import com.lemon.Utils.JDBCUtil;

/**手机号码生成器
 * @author lym
 *
 */
public class MobileGenerator {
	/**返回待注册的手机号
	 * @return
	 */
	public String toBeRegisterMobilephone(){
		String sql="select concat(max(mobilephone+1),'') as mobilephone from member";
		try {
			Map<String, Object> map = JDBCUtil.query(sql, null);
			String value = map.get("mobilephone").toString();
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**返回还未注册的手机号
	 * @return
	 */
	public String notRegisterYetMobilephone(){
		String sql="select concat(max(mobilephone+2),'') as mobilephone from member";
		try {
			Map<String, Object> map = JDBCUtil.query(sql, null);
			String value= map.get("mobilephone").toString();
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
