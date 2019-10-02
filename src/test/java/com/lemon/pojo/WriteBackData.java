package com.lemon.pojo;

public class WriteBackData {
	private String rowIdentifier;
	private String cellName;
	private String result;
	private String sheetName;
	
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
