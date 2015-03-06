package com.ttianjun.common.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Lists {
	/**
	 * List<Class<? extends T>> classList = Lists.newArrayList();
	 */
	public static <T> ArrayList<T> newArrayList(){
		return new ArrayList<T>();
	}
	
	/**
	 * Map<String, List<String>> m = Lists.newHashMap();   
	 */
	public static <K, V> HashMap<K, V> newHashMap(){    
	    return new HashMap<K, V>();    
	}   
}
