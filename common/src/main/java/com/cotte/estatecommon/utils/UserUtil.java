package com.cotte.estatecommon.utils;

import com.alibaba.fastjson.JSONObject;
import com.cotte.estatecommon.Subject;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: UserInfoPool
 * @author: falcomlife
 * @create: 2019/07/15
 * @version: 1.0.0
 */
public class UserUtil {

	
	private static ThreadLocal<JSONObject> userInfoThreadLocal = new ThreadLocal<>();
	private static Map<String,Object> map = new HashMap<String,Object>();
	public static void setInfo(JSONObject jo) {
		map.put(Thread.currentThread().getThreadGroup().getName()+","+Thread.currentThread().getName()+","+Thread.currentThread().getPriority()+","+Thread.currentThread().getId(),jo);
	}

	public static JSONObject getInfo() {
		return (JSONObject) map.get(Thread.currentThread().getThreadGroup().getName()+","+Thread.currentThread().getName()+","+Thread.currentThread().getPriority()+","+Thread.currentThread().getId());
	}

	public static void remove(){
	    map.remove(Thread.currentThread().getThreadGroup().getName()+","+Thread.currentThread().getName()+","+Thread.currentThread().getPriority()+","+Thread.currentThread().getId());
    }

	/**
	 * 获取userid
	 * @return
	 */
	public static String getUserId(){
		return (String) getInfo().get("userId");
	}

	/**
	 * 获取account
	 * @return
	 */
	public static String getAccount(){
		return (String) getInfo().get("username");
	}

	/**
	 * 获取租户
	 * @return
	 */
	public static String getTenantId(){
		return (String) getInfo().get("tenantId");
	}

	/**
	 * 存端
	 * @return
	 */
	public static void setSource(String source){
		map.put(Thread.currentThread().getThreadGroup().getName()+","+Thread.currentThread().getName()+","+Thread.currentThread().getPriority()+","+Thread.currentThread().getId()+",source",source);
	}

	/**
	 * 获取端
	 * @return
	 */
	public static String getSource(){
		return (String) map.get(Thread.currentThread().getThreadGroup().getName()+","+Thread.currentThread().getName()+","+Thread.currentThread().getPriority()+","+Thread.currentThread().getId()+",source");
	}
}
