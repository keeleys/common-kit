package com.ttianjun.common.kit.parse;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ttianjun.common.kit.Lists;
 
/**
 * json 简单操作的工具类
 * @author TianJun
 *
 */
public class JsonUtil{
 
    private JsonUtil(){}
    public static XJDataNode parseJson(String str){
    	JSONObject root= JSON.parseObject(str);
    	return toDateNode(root);
    }
    private  static XJDataNode toDateNode(JSONObject jsonObject){
    	XJDataNode dataNode = new XJDataNode();
    	for(String key : jsonObject.keySet()){
    		Object o =jsonObject.get(key);
    		if(o instanceof JSONObject){
    			List<XJDataNode> nodeList = Lists.newArrayList();
    			nodeList.add(toDateNode((JSONObject) o));
    			dataNode.put(key, nodeList);
    		}else if(o instanceof JSONArray){
    			List<XJDataNode> nodeList = Lists.newArrayList();
    			JSONArray arr = (JSONArray) o;
    			for(int i = 0 ;i<arr.size();i++){
    				nodeList.add(toDateNode(arr.getJSONObject(i)));
    			}
    			dataNode.put(key, nodeList);
    		}else{
    			dataNode.put(key,o.toString()); 
    		}
    	}
    	return dataNode;
    }
}