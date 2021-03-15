package com.ecommerce.dtos.paytm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paytm")
public class PaytmConfig {

	private String mid;
	private String website;
	private String callbackUrl;
	private String key;
	private String successRedirectUrl;
	private String failureRedirectUrl;
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSuccessRedirectUrl() {
		return successRedirectUrl;
	}
	public void setSuccessRedirectUrl(String successRedirectUrl) {
		this.successRedirectUrl = successRedirectUrl;
	}
	public String getFailureRedirectUrl() {
		return failureRedirectUrl;
	}
	public void setFailureRedirectUrl(String failureRedirectUrl) {
		this.failureRedirectUrl = failureRedirectUrl;
	}
	
}
