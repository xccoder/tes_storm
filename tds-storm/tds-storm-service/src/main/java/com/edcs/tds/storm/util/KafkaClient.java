package com.edcs.tds.storm.util;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaClient<K, V> {
	
	final Logger logger = LoggerFactory.getLogger(KafkaClient.class);

	private Properties properties;

	private String defaultTopic;

	private KafkaProducer<K, V> producer;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setDefaultTopic(String defaultTopic) {
		this.defaultTopic = defaultTopic;
	}

	public void setProducer(KafkaProducer<K, V> producer) {
		this.producer = producer;
	}

	public void init() {
		if (properties == null) {
			throw new NullPointerException("kafka properties is null.");
		}
		this.producer = new KafkaProducer<K, V>(properties);
	}

	public void syncSend(V value) {
		ProducerRecord<K, V> producerRecord = new ProducerRecord<K, V>(defaultTopic, value);
		try {
			producer.send(producerRecord).get(100, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			logger.error("Sent message times out, topic name: {}, try sending the message again, error: " + e,
					defaultTopic);
			// Attempts to resend the message
			int count = 0;
			for (int i = 0; i < 5; i++) {
				try {
					count++;
					logger.warn("Attempts to resend the message, topic name: {}, count: {}.", defaultTopic, count);
					RecordMetadata metadata = producer.send(producerRecord).get(3, TimeUnit.SECONDS);
					if (metadata != null) {
						break;
					}
				} catch (InterruptedException e1) {
				} catch (ExecutionException e2) {
				} catch (TimeoutException e3) {
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public void asyncSend(V value) {
		ProducerRecord<K, V> producerRecord = new ProducerRecord<K, V>(defaultTopic, value);
		producer.send(producerRecord);
	}
}