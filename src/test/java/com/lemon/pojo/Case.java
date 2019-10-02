package com.lemon.pojo;

public class Case {
	private String caseId;
	private String apiId;
	private String params;
	private String expectedResponseData;
	private String actualResponseData;
	private String preValidateSql;
	private String preValidateResult;
	private String afterValidateSql;
	private String afterValidateResult;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getExpectedResponseData() {
		return expectedResponseData;
	}
	public String getActualResponseData() {
		return actualResponseData;
	}
	public void setActualResponseData(String actualResponseData) {
		this.actualResponseData = actualResponseData;
	}
	public void setExpectedResponseData(String expectedResponseData) {
		this.expectedResponseData = expectedResponseData;
	}
	public String getPreValidateSql() {
		return preValidateSql;
	}
	public void setPreValidateSql(String preValidateSql) {
		this.preValidateSql = preValidateSql;
	}
	public String getPreValidateResult() {
		return preValidateResult;
	}
	public void setPreValidateResult(String preValidateResult) {
		this.preValidateResult = preValidateResult;
	}
	public String getAfterValidateSql() {
		return afterValidateSql;
	}
	public void setAfterValidateSql(String afterValidateSql) {
		this.afterValidateSql = afterValidateSql;
	}
	public String getAfterValidateResult() {
		return afterValidateResult;
	}
	public void setAfterValidateResult(String afterValidateResult) {
		this.afterValidateResult = afterValidateResult;
	}
	
	
}
