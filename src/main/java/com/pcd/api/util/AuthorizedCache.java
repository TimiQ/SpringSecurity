/**
 * 
 */
package com.pcd.api.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TimiQ
 *
 */
public class AuthorizedCache {

	public static Map<String, String> REDIS_MAP = new HashMap<String, String>();
	public static Map<String, String> USER_MAP = new HashMap<String, String>();
	
	public static String getUserMap(String userName){
		Map<String,String> map = USER_MAP;
		return map.get(userName);
	}

}
