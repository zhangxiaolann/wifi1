package com.liuguilin.only.bean;

/**
 * 微信精选
 * 
 * @author LGL
 *
 */
public class WechatBean {

	private String url;
	private String title;
	private String type;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "WechatBean [url=" + url + ", title=" + title + ", type=" + type
				+ "]";
	}
}
