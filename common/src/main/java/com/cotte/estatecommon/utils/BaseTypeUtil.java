package com.cotte.estatecommon.utils;

import cn.hutool.core.bean.BeanUtil;
import com.cotte.estatecommon.RS;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 类型转换的工具类
 * @author jiangkd
 * @date 2019-02-20 09:31
 */
public class BaseTypeUtil {

	public static Double nanDouble(Object d) {
		if (d == null) {
			return Double.NaN;
		}
		try {
			return Double.parseDouble(d.toString());
		} catch (RuntimeException e) {
			return 0d;
		}
	}

	public static Double safeDouble(Object d) {
		if (d == null) {
			return 0d;
		}
		try {
			return Double.parseDouble(d.toString());
		} catch (RuntimeException e) {
			return 0d;
		}
	}

	public static int safeInt(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Double) {
			return safeDouble(obj).intValue();
		}

		try {
			return Integer.parseInt(String.valueOf(obj));
		} catch (RuntimeException e) {
			return 0;
		}
	}

	public static String safeString(Object obj) {
		if (obj == null) {
			return "";
		}
		return String.valueOf(obj);
	}

	public static Long safeLong(Object obj) {
		if (obj == null) {
			return 0L;
		}
		if (obj instanceof Double) {
			return safeDouble(obj).longValue();
		}

		try {
			return Long.parseLong(String.valueOf(obj));
		} catch (RuntimeException e) {
			return 0L;
		}
	}

	public static byte[] encodeInt(int num){
		byte[] result = new byte[4];
		result[0] = (byte)((num >>> 24) ^ 0x80);
		result[1] = (byte)((num >>> 16));
		result[2] = (byte)((num >>> 8));
		result[3] = (byte)((num));
		return result;
	}

	public static Map<String,Object> beanToMap(RS result){
		Map<String,Object> map = new HashMap<>();
		if(result.getS() == 0){
			final Object rs = result.getRs();
			if(rs != null){
//				将对象转换为map
				map = BeanUtil.beanToMap(rs);
			}
		}
		return map;
	}
}
