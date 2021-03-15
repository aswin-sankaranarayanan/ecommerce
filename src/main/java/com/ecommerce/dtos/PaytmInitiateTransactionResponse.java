package com.ecommerce.dtos;

import com.ecommerce.dtos.paytm.Body;

public class PaytmInitiateTransactionResponse {

	private Head head;
	private Body body;
	
	public Head getHead() {
		return head;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "PaytmInitiateTransactionResponse [head=" + head + ", body=" + body + "]";
	}
	
	
}


class Head {
	private String responseTimestamp;
	private String version;
	private String clientId;
	private String signature;
	public String getResponseTimestamp() {
		return responseTimestamp;
	}
	public void setResponseTimestamp(String responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "Head [responseTimestamp=" + responseTimestamp + ", version=" + version + ", clientId=" + clientId
				+ ", signature=" + signature + "]";
	}
	
	
}



