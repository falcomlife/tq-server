package com.cotte.estatecommon.pool.multifunctional;

public interface Client {
	
	void create();
	
	Object exec(Object ... object) throws Exception;
	
	void destory();
	
	void close();

}
