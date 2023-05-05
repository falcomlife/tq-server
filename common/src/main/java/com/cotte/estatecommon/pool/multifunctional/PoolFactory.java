package com.cotte.estatecommon.pool.multifunctional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PoolFactory {

	public <T> Pool createPool(Class clazz, PoolManager poolManager, Config config)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Pool pool = Pool.createPool(config.getSize());
		for (int i = 0; i < config.getSize(); i++) {
			Client client = (Client) clazz.getConstructor(Integer.class, PoolManager.class, Config.class)
					.newInstance(i, poolManager, config);
			pool.addItemToFree(i, client);
		}
		return pool;
	}

}
