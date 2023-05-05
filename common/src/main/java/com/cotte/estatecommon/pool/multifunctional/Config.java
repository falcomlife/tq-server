package com.cotte.estatecommon.pool.multifunctional;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
	
	private String aepAppKey;
	private String aepAppSecret;
	private Integer size;
	private Integer requestSize;
	
}
