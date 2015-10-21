package com.ttianjun.common.kit.weixin;

import com.ttianjun.common.kit.Prop;
import com.ttianjun.common.kit.PropKit;

public class WeixinContent {
	public static final Prop weixinProp = PropKit.use("weixin.txt");
	
	public static final String APPID =weixinProp.get("appid");
	public static final String SECRET =weixinProp.get("secret");
	
	public static final String APPID2 =weixinProp.get("appid2");
	public static final String SECRET2 =weixinProp.get("secret2");
	
	
	public static final String NONCESTR =weixinProp.get("noncestr");
	public static final String JSAPIURL =weixinProp.get("jsapi_url");
	
	public static final String IMAGE_URL =weixinProp.get("image_url");
	
	public static final String LOGO_URL =weixinProp.get("logo_url");
	
	
	
}
