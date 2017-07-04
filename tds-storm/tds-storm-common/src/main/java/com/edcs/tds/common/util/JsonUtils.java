package com.edcs.tds.common.util;


import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json 工具类
 * @author LiQF
 *
 */
public class JsonUtils {
	
	/**
	 * 将json字符串转化为JSONObject对象
	 * @param text
	 * @return
	 */
	public static final JSONObject  parseObject(String text){
		return JSON.parseObject(text);
	}
	/**
	 * 将json字符串转化为指定的object的对象
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> cls){
		T object = JSON.parseObject(json,cls);
		return object;
	}
	
	/**
	 * 将Object对象转化为json字符串
	 * @param obj
	 * @return
	 */
    public static String toJson(Object obj){
    	String json = JSON.toJSONString(obj,SerializerFeature.DisableCircularReferenceDetect);
    	return json;
    }
    
    /**
     * 将json字符串转化为list对象
     */
    public static <T> List<T> toArray(String json,Class<T> cls){
    	List<T> lists = JSON.parseArray(json, cls);
    	return lists;
    }
}
