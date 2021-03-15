package com.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ORDER_TRANSACTION_DETAILS")
public class OrderTransactionDetails extends BaseEntity {
	
	@Column(name="TXN_AMOUNT")
	private String txnAmount;
	
	@Column(name="CURRENCY")
	private String currency;
	
	@Column(name="TXNDATE")
	private String txnDate;
	
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="RESPMSG")
	private String respMsg;
	
	@Column(name="BANKNAME")
	private String bankName;
	
	@Column(name="BANKTXNID")
	private String bankTXID;
	
	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankTXID() {
		return bankTXID;
	}

	public void setBankTXID(String bankTXID) {
		this.bankTXID = bankTXID;
	}

}
