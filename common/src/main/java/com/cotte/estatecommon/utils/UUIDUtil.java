package com.cotte.estatecommon.utils;

import cn.hutool.core.util.IdUtil;

/**
 * @ClassName UuidUtil
 * @description: 产生uuid
 * @author: ygj
 * @date: 2022-03-28 13:13
 */
public class UUIDUtil {


    /**
     * 生成uuid
     * @return
     */
    public static String simpleUUid(){

        return IdUtil.simpleUUID();
    }
}
