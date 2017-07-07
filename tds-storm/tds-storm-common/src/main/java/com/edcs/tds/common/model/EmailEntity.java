package com.edcs.tds.common.model;

import java.io.Serializable;
import java.util.List;

public class EmailEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8372498567815338715L;
	
	private List<String> receiveAccounts;//收件邮箱帐号
	private String sendServer;//发件服务器
	private String port;//发件服务器端口号
	private String sendAccount;//发件邮箱帐号
	private String emailPassword;//发件邮箱密码
	private boolean booeanSsl;//是否使用SSL 加密发送
	private String content;//发送邮件的内容
	public List<String> getReceiveAccounts() {
		return receiveAccounts;
	}
	public void setReceiveAccounts(List<String> receiveAccounts) {
		this.receiveAccounts = receiveAccounts;
	}
	public String getSendServer() {
		return sendServer;
	}
	public void setSendServer(String sendServer) {
		this.sendServer = sendServer;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSendAccount() {
		return sendAccount;
	}
	public void setSendAccount(String sendAccount) {
		this.sendAccount = sendAccount;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	public boolean isBooeanSsl() {
		return booeanSsl;
	}
	public void setBooeanSsl(boolean booeanSsl) {
		this.booeanSsl = booeanSsl;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	

}
