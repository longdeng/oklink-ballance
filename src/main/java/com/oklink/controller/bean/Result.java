package com.oklink.controller.bean;

import com.alibaba.fastjson.JSON;

public class Result {
	private int resultCode;
	private int errorNum;
	private long objectId;
	
	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	public int getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}
