package com.ssaglobal.scm.wms.util.attribute;

public class TypeInfo {

	String storerkey;
	String type;
	String attribute;

	public TypeInfo(String storerkey, String type, String attribute) {
		super();
		this.storerkey = storerkey;
		this.type = type;
		this.attribute = attribute;
	}

	public String getStorerkey() {
		return storerkey;
	}

	public void setStorerkey(String storerkey) {
		this.storerkey = storerkey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

}
