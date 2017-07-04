package com.edcs.tds.storm.topology;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.edcs.tds.storm.util.DataSerializer;
import com.edcs.tds.storm.util.KafkaClient;

public class KafkaClientTest {

	public static void main(String[] args) {
		
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "172.26.66.32:6667");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		KafkaClient<byte[], byte[]> kafkaClient = new KafkaClient<byte[], byte[]>();
		kafkaClient.setDefaultTopic("test");
		kafkaClient.setProperties(properties);
		kafkaClient.init();
		
		
//		FSTConfiguration fstConf = FSTConfiguration.getDefaultConfiguration();
		long nowDate = System.currentTimeMillis();

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("123", "1");
		maps.put("2222", "1588888888");
		maps.put("c", nowDate);
		kafkaClient.syncSend(DataSerializer.asByteArrayForJson(maps));
		System.out.println("Send Succ.................");

	}

}
