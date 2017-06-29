package com.edcs.tds.storm.util;

import java.io.Serializable;

import org.nustaq.serialization.FSTConfiguration;

import com.google.common.base.Charsets;

public class DataSerializer implements Serializable {

	private static final long serialVersionUID = -5286985724029020883L;

	private static FSTConfiguration fstConfiguration = FSTConfiguration.createDefaultConfiguration();

	private static FSTConfiguration jsonConfiguration = FSTConfiguration.createJsonConfiguration();

	public static FSTConfiguration getDefaultFSTConfiguration() {
		return fstConfiguration;
	}

	public static FSTConfiguration getJsonFSTConfiguration() {
		return jsonConfiguration;
	}

	public static Object asObjectForDefault(byte[] b) {
		try {
			return fstConfiguration.asObject(b);
		} catch (Exception e) {
			throw new RuntimeException(new String(b, Charsets.UTF_8), e);
		}
	}

	public static Object asObjectForJson(byte[] b) {
		try {
			return jsonConfiguration.asObject(b);
		} catch (Exception e) {
			throw new RuntimeException(new String(b, Charsets.UTF_8), e);
		}
	}

	public static byte[] asByteArrayForDefault(Object o) {
		return fstConfiguration.asByteArray(o);
	}

	public static byte[] asByteArrayForJson(Object o) {
		return jsonConfiguration.asByteArray(o);
	}

}
