package com.edcs.tds.common.engine.groovy.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.util.MD5Util;
import com.google.common.base.Charsets;

/**
 * Groovy 通用工具类
 */
public class EngineUtil {

	private static final Logger logger = LoggerFactory.getLogger(EngineUtil.class);

	public static String encodeBase64(String text) {
		String result = null;
		try {
			if (StringUtils.isNotEmpty(text)) {
				result = new String(Base64.encodeBase64(text.getBytes(Charsets.UTF_8.name())), Charsets.UTF_8);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;
	}

	public static String decodeBase64(String base64String) {
		String result = null;
		try {
			if (StringUtils.isNotEmpty(base64String)) {
				result = new String(Base64.decodeBase64(base64String.getBytes(Charsets.UTF_8.name())),
						Charsets.UTF_8.name());
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return result;
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}

	public static String formatTime(String pattern) {
		return new DateTime().toString(pattern);
	}

	public static String formatTime(Date date, String pattern) {
		return new DateTime(date).toString(pattern);
	}

	public static String md5(String plainText) {
		return MD5Util.getMd5(plainText);
	}

}