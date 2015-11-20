package com.ttianjun.common.kit.parse;

import java.util.List;
import java.util.Map;

import com.ttianjun.common.kit.Lists;

/**
 * json xml通用bean<br/>
 * json的直接get(key) <br/>
 * xml的所有都是节点 所以需要getNode("key").get("text") 或者 getNode("key").get("attrName");
 * @author TianJun
 * time 2015年3月2日
 */
public class XJDataNode {
	
	private Map<String,String> propMap;
	private Map<String,List<XJDataNode>> childNode;
	
	public XJDataNode() {
		super();
		propMap = Lists.newHashMap();
		childNode = Lists.newHashMap();
	}
	public String get(String key){
		return propMap.get(key);
	}
	public List<XJDataNode> getNodeList(String key){
		return childNode.get(key);
	}
	public XJDataNode getNode(String key){
		List<XJDataNode> nodeList = childNode.get(key);
		if(nodeList!=null && !nodeList.isEmpty())
			return nodeList.get(0);
		return null;
	}
	public void put(String key,String value){
		propMap.put(key,value);
	}
	public void put(String key,List<XJDataNode> nodeList){
		if(childNode.containsKey(key))
			nodeList.addAll(nodeList);
		else
			childNode.put(key,nodeList);
	}
}

