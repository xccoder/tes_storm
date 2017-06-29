package com.edcs.tds.common.redis;

import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class ProxyJedisPool extends Pool<Jedis> {

	public ProxyJedisPool(final GenericObjectPoolConfig poolConfig, final List<String> addressList) {
		this(poolConfig, addressList, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
	}

	public ProxyJedisPool(final GenericObjectPoolConfig poolConfig, final List<String> addressList, final int timeout) {
		this(poolConfig, addressList, timeout, null, Protocol.DEFAULT_DATABASE, null);
	}

	public ProxyJedisPool(final GenericObjectPoolConfig poolConfig, final List<String> addressList, final int timeout,
			final String password, final int database) {
		this(poolConfig, addressList, timeout, password, database, null);
	}

	public ProxyJedisPool(final GenericObjectPoolConfig poolConfig, final List<String> addressList, final int timeout,
			final String password, final int database, final String clientName) {
		super(poolConfig, new ProxyJedisFactory(addressList, timeout, password, database, clientName));
	}

	@Override
	public Jedis getResource() {
		Jedis jedis = super.getResource();
		jedis.setDataSource(this);
		return jedis;
	}

	/**
	 * @deprecated starting from Jedis 3.0 this method won't exist. Resouce
	 *             cleanup should be done using @see
	 *             {@link redis.clients.jedis.Jedis#close()}
	 */
	@Deprecated
	public void returnBrokenResource(final Jedis resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	/**
	 * @deprecated starting from Jedis 3.0 this method won't exist. Resouce
	 *             cleanup should be done using @see
	 *             {@link redis.clients.jedis.Jedis#close()}
	 */
	@Deprecated
	public void returnResource(final Jedis resource) {
		if (resource != null) {
			try {
				resource.resetState();
				returnResourceObject(resource);
			} catch (Exception e) {
				returnBrokenResource(resource);
				throw new JedisException("Could not return the resource to the pool", e);
			}
		}
	}
}
