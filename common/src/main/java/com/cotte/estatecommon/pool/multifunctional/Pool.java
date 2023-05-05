package com.cotte.estatecommon.pool.multifunctional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Pool {

	private Map<Integer, Client> poolFree;
	private Map<Integer, Client> poolUsed;
	private Integer size;
	private static int DEFAULT_POOL_SIZE = 20;

	private Pool(int size) {
		this.size = size;
		this.poolFree = new ConcurrentHashMap<>(size);
		this.poolUsed = new ConcurrentHashMap<>(size);
	}

	public static Pool createPool() {
		return new Pool(Pool.DEFAULT_POOL_SIZE);
	}

	public static Pool createPool(Integer size) {
		if (size == null) {
			return createPool();
		}
		return new Pool(size);
	}

	public Integer getSize() {
		return size;
	}

	public Map<Integer, Client> getPoolFree() {
		return poolFree;
	}

	public Map<Integer, Client> getPoolUsed() {
		return poolUsed;
	}

	public void addItemToFree(Integer no, Client client) {
		this.poolFree.put(no, client);
		System.out.println("+ free pool size is " + this.poolFree.size() + " now.");
	}

	public void removeItemFromFree(Integer no) {
		this.poolFree.remove(no);
		System.out.println("- free pool size is " + this.poolFree.size() + " now.");
	}

	public Client getItemFromFree(Integer no) {
		return this.poolFree.get(no);
	}

	public void addItemToUsed(Integer no, Client client) {
		this.poolUsed.put(no, client);
		System.out.println("+ used pool size is " + this.poolUsed.size() + " now.");
	}

	public void removeItemFromUsed(Integer no) {
		this.poolUsed.remove(no);		
		System.out.println("- used pool size is " + this.poolUsed.size() + " now.");
		System.out.println("report pool used status " + this.poolUsed );
	}

	public Client getItemFromUsed(Integer no) {
		return this.poolUsed.get(no);
	}
}
