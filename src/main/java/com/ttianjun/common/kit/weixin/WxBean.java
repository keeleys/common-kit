package com.ttianjun.common.kit.weixin;

import java.io.Serializable;
/**
 * 微信返回的json解析成实体bean
 * @author jun
 *
 */
public class WxBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String access_token;
	public String openid;
	
	public String errcode;
	public String errmsg;
}
