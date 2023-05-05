package com.cotte.estatecommon.pool.multifunctional;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class PoolManager {

	private Pool pool;
	private Config config;
	private Class clazz;

	public PoolManager(Class clazz, Config config) {
		if (config.getSize() < config.getRequestSize()) {
			throw new RuntimeException("request size must smaller than size.");
		}
		this.pool = pool;
		this.config = config;
		this.clazz = clazz;
	}

	public void createPool() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		this.pool = new PoolFactory().createPool(this.clazz, this, this.config);
	}

	/**
	 * 借出client,放入used pool
	 * 
	 * @return
	 */
	public Client lendClient() {
		Integer no = this.pool.getPoolFree().size() - 1;
		Client client = this.pool.getPoolFree().get(no);
		this.pool.removeItemFromFree(no);
		this.pool.addItemToUsed(no, client);
		this.supplementClient();
		return client;
	}

	/**
	 * 补充client到free池中
	 */
	private void supplementClient() {
		// 如果free池中client数量小于request size, 则创建新的client, 使数量达到size要求.
		Integer currentSize = this.pool.getPoolFree().size();
		if (currentSize <= this.config.getRequestSize()) {
			System.out.println("used pool size is " + this.pool.getPoolFree().size() + " now, but request size is "
					+ this.config.getRequestSize());
			System.out.println("suppling...");
			for (int i = 0; i < this.config.getSize(); i++) {
				if (this.pool.getPoolFree().containsKey(i)) {
					continue;
				}
				Client clientNew = null;
				try {
					clientNew = (Client) this.clazz.getConstructor(Integer.class, PoolManager.class, Config.class)
							.newInstance(i, this, config);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
				this.pool.addItemToFree(i, clientNew);
			}
			System.out.println("supplement finish , now free pool size is " + this.pool.getPoolFree().size());
		}
	}

	/**
	 * 关闭client,移除used pool
	 * 
	 * @param index
	 */
	public void closeClient(Integer no) {
		this.pool.removeItemFromUsed(no);
		try {
			Client clientNew = (Client) this.clazz.getConstructor(Integer.class, PoolManager.class, Config.class)
					.newInstance(no, this, config);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
