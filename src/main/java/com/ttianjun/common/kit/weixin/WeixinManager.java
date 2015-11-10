package com.ttianjun.common.kit.weixin;

import java.util.LinkedHashMap;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ttianjun.common.kit.http.HttpKit;
import com.ttianjun.common.kit.security.DecriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * http://mp.weixin.qq.com/debug/
 * @author jun
 *
 */
public class WeixinManager {
	private static Logger log = LoggerFactory.getLogger(WeixinManager.class);
	/**
	 * 基础支持: 获取access_token接口 /token
	 * 有效期7200秒
	 */
	public static String getAccessToken(){
		//每7000秒获取一次
		return CacheKit.get("weixin", "accessToken", new IDataLoader() {
			public Object load() {
				String url = "https://api.weixin.qq.com/cgi-bin/token";
				Map<String,String> queryParas = new LinkedHashMap<String,String>();
				queryParas.put("grant_type", "client_credential");
				queryParas.put("appid", WeixinContent.APPID);
				queryParas.put("secret", WeixinContent.SECRET);
				String jsonStr = null;
				try{
					jsonStr = HttpKit.get(url, queryParas);
				}catch(Exception e){
					log.error("获取accessToken http请求错误",e);
					return null;
				}
				
				log.info("从微信获取access_token,返回报文:"+jsonStr);
				
				Map<String, String> resultMap2 = new Gson().fromJson( jsonStr, 
						new TypeToken<Map<String, String>>() { }.getType());
				String accessToken = resultMap2.get("access_token");
				if(accessToken==null){
					CacheKit.remove("weixin", "accessToken");
				}
				return accessToken;
			}
		});
		
	}
	/**
	 * jsapi_ticket是公众号用于调用微信JS接口的临时票据 有效期7200秒
	 * @return jsapi_ticket
	 */
	public static String getJsapiTicket(){
		//每7000秒获取一次
		return CacheKit.get("weixin", "jsapiTicket", new IDataLoader() {
			public Object load() {
				String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
				Map<String,String> queryParas = new LinkedHashMap<String,String>();
				queryParas.put("access_token", WeixinManager.getAccessToken());
				queryParas.put("type", "jsapi");
				String jsonStr=null;
				try{
					jsonStr = HttpKit.get(url, queryParas);
				}catch(Exception e){
					log.error("获取jsapi_ticket http请求错误",e);
					return null;
				}
				log.info("从微信获取jsapi_ticket,返回报文:"+jsonStr);
				
				Map<String, String> resultMap2 = new Gson().fromJson( jsonStr, 
						new TypeToken<Map<String, String>>() { }.getType());
				
				String errmsg = resultMap2.get("errmsg");
				if("ok".equals(errmsg)){
					return resultMap2.get("ticket");
				}
				CacheKit.remove("weixin", "accessToken");
				return null;
			}
		});
	}
	/**
	 * 获取jsapi 签名
	 * @param url 对应的页面
	 * @return signature
	 */
	public static String getJsSignature(String timestamp,String url){
		StringBuffer source = new StringBuffer();
		source.append("jsapi_ticket").append("=").append(WeixinManager.getJsapiTicket());
		source.append("&noncestr").append("=").append(WeixinContent.NONCESTR);
		source.append("&timestamp").append("=").append(timestamp);
		source.append("&url").append("=").append(url);
		return DecriptUtil.SHA1(source.toString());
	}
	
	
	/**
	 * 基础支持: 获取 网页授权access_token
	 * 有效期7200秒
	 * {
	   "access_token":"ACCESS_TOKEN",
	   "expires_in":7200,
	   "refresh_token":"REFRESH_TOKEN",
	   "openid":"OPENID",
	   "scope":"SCOPE",
	   "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
		}
	 */
	public static WxBean getAccessTokenOauth2(final String code){
		final String catchKey = WeixinContent.APPID+"accessTokenOauth2"+code;
		//每7000秒获取一次
		return CacheKit.get("weixin", catchKey, new IDataLoader() {
			public Object load() {
				String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
				Map<String,String> queryParas = new LinkedHashMap<String,String>();
				queryParas.put("appid", WeixinContent.APPID2);
				queryParas.put("secret", WeixinContent.SECRET2);
				queryParas.put("code", code);
				queryParas.put("grant_type", "authorization_code");
				String jsonStr = null;
				try{
					jsonStr = HttpKit.get(url, queryParas);
				}catch(Exception e){
					log.error("获取AccessTokenOauth2 http请求错误",e);
					return null;
				}
				
				log.info("从微信获取AccessTokenOauth2,返回报文:"+jsonStr);
				
				WxBean model = new Gson().fromJson( jsonStr,WxBean.class);
				if(model.errmsg==null){
					return model;
				}
				CacheKit.remove("weixin", catchKey);
				return null;
			}
		});
		
	}
}
