package com.ecommerce.dtos.paytm;


public class Body {
	
	private ResultInfo resultInfo;
	private String txnToken;
	
	public ResultInfo getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}
	public String getTxnToken() {
		return txnToken;
	}
	public void setTxnToken(String txnToken) {
		this.txnToken = txnToken;
	}
	@Override
	public String toString() {
		return "Body [resultInfo=" + resultInfo + ", txnToken=" + txnToken + "]";
	}
	
	
}