package com.edcs.tds.common.redis;

import com.google.common.base.Joiner;

import redis.clients.util.SafeEncoder;

public class RedisCacheKey {

    private static Joiner joiner = Joiner.on(":").skipNulls();

    public static final String ROOT = "TES";
    public static final String CONFIG = "CONFIG";
    public static final String FILTER = "FILTER";

    public static byte[] getRuleConfig() {
        return SafeEncoder.encode(joiner.join(ROOT, CONFIG, "RULE"));
    }

    public static String getRuleConfigVersion() {
        return joiner.join(ROOT, CONFIG, "RULE", "VERSION");
    }

    public static String getMDProcessKey() {
        return joiner.join(ROOT, CONFIG, "PRO");
    }

    public static String getMDStepKey() {
        return joiner.join(ROOT, CONFIG, "STEP");
    }

    public static String getMDSubKey() {
        return joiner.join(ROOT, CONFIG, "SUB");
    }

    public static String getMDChangeKey() {
        return joiner.join(ROOT, CONFIG, "CHANGE");
    }

    public static String getFilterKey(String key) {
        return joiner.join(ROOT, FILTER, key);
    }

    public static String getDataSyncKey() {
        return ROOT+"-"+"RESULT";
    }

}