package com.edcs.tds.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Maps;

public class TupleMap<K, V> extends ForwardingMap<K, V> implements Serializable {

	private static final long serialVersionUID = -8742961697746869111L;

	private Map<K, V> dataMap = null;

	public TupleMap() {
		this(Maps.<K, V> newHashMap());
	}

	public TupleMap(Map<K, V> dataMap) {
		super();
		this.dataMap = dataMap;
	}

	public TupleMap(List<K> columns) {
		this.dataMap = Maps.<K, V> newHashMap();
		for (K c : columns) {
			this.dataMap.put(c, null);
		}
	}

	@Override
	protected Map<K, V> delegate() {
		return dataMap;
	}

	public Map<K, V> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<K, V> dataMap) {
		this.dataMap = dataMap;
	}

	public String getString(String key) {
		return (String) super.get(key);
	}

	public Integer getInteger(String key) {
		return (Integer) super.get(key);
	}

	public BigDecimal getBigDecimal(String key) {
		return (BigDecimal) super.get(key);
	}

	public Date getDate(String key) {
		return (Date) super.get(key);
	}

	@Override
	public String toString() {
		return dataMap + "";
	}

}
