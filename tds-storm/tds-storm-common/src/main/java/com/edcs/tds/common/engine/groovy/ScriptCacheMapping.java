package com.edcs.tds.common.engine.groovy;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.edcs.tds.common.util.MD5Util;
import com.google.common.collect.Maps;

import groovy.lang.Script;

public class ScriptCacheMapping implements Serializable {

	private static final long serialVersionUID = -6401408716509581348L;

	private static ConcurrentMap<String, Pair<String, Script>> scriptCache = Maps.newConcurrentMap();

	public ConcurrentMap<String, Pair<String, Script>> getScriptCache() {
		return scriptCache;
	}

	public void addScript(String id, String hash, Script obj) {
		if (!contains(id)) {
			scriptCache.put(id, Pair.of(hash, obj));
		}
	}

	public boolean contains(String id) {
		return scriptCache.containsKey(id);
	}

	public boolean isDifference(String id, String hash) {
		if (!contains(id)) {
			return false;
		}
		return scriptCache.get(id).getLeft().equals(hash);
	}

	public Script getScript(String id) {
		if (!contains(id)) {
			return null;
		}
		return scriptCache.get(id).getRight();
	}

	public String getScriptHash(String scriptContent) {
		if (StringUtils.isNotEmpty(scriptContent)) {
			return MD5Util.getMd5(scriptContent);
		}
		return null;
	}

	public void remove(String id) {
		if (contains(id)) {
			scriptCache.remove(id);
		}
	}

}
