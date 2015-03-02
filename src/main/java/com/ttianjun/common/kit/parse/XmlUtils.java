package com.ttianjun.common.kit.parse;


import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ttianjun.common.kit.Lists;

public class XmlUtils {
    public static XJDataNode parseXml(String xml) throws DocumentException{
    	Document doc = DocumentHelper.parseText(xml);
    	Element root = doc.getRootElement();  
		return toDateNode(root);
    }
    
    private  static XJDataNode toDateNode(Element root){
    	XJDataNode dataNode = new XJDataNode();
    	
    	if(root.elements().size() == 0){
			dataNode.put("text", root.getText());
		}
    	
    	for(Object o : root.attributes()){
    		Attribute attr =(Attribute) o;
    		dataNode.put(attr.getName(), attr.getValue());
    	}
    	for(Object o:root.elements()){
    		Element element = (Element) o;
			List<XJDataNode> nodeList = Lists.newArrayList();
			nodeList.add(toDateNode(element));
			dataNode.put(element.getName(), nodeList);
    	}
    	
    	return dataNode;
    }
}
