package com.oklink.api.bean;


public class WalletView {

	private long id;// 数据库表中记录唯一标识id
	private long user_id;// 用户Id
	private String name;// 钱包名称
	private Amount btc_balance;// 非冻结bitcoin余额
	private Amount ltc_balance;// 非冻结人民币余额

	private boolean is_default; // 是否默认 0：否 1：是
	private String btc_address;// btc地址
	private String ltc_address;// ltc地址
	
	private String created_at;// 创建时间
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Amount getBtc_balance() {
		return btc_balance;
	}

	public void setBtc_balance(Amount btc_balance) {
		this.btc_balance = btc_balance;
	}

	public Amount getLtc_balance() {
		return ltc_balance;
	}

	public void setLtc_balance(Amount ltc_balance) {
		this.ltc_balance = ltc_balance;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


	public boolean isIs_default() {
		return is_default;
	}


	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}


	public String getBtc_address() {
		return btc_address;
	}


	public void setBtc_address(String btc_address) {
		this.btc_address = btc_address;
	}


	public String getLtc_address() {
		return ltc_address;
	}


	public void setLtc_address(String ltc_address) {
		this.ltc_address = ltc_address;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getUser_id() {
		return user_id;
	}



	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}



}
