package com.lemon.pojo;

import java.util.Map;

public class DBCheckerResult {
	private String no;
	private Map<String, Object> columnLablelAndValue;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Map<String, Object> getColumnLablelAndValue() {
		return columnLablelAndValue;
	}
	public void setColumnLablelAndValue(Map<String, Object> columnLablelAndValue) {
		this.columnLablelAndValue = columnLablelAndValue;
	}
	
}
