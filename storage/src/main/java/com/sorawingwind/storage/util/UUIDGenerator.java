package com.sorawingwind.storage.util;

import com.cotte.estatecommon.utils.UUIDUtil;
import io.ebean.config.IdGenerator;
import org.springframework.stereotype.Component;

/**
 * Ebean 主键为uuid
 */
@Component
public class UUIDGenerator implements IdGenerator {

    @Override
    public Object nextValue() {
        // 返回生成的UUID值
        return UUIDUtil.simpleUUid();
    }

    @Override
    public String getName() {
        // 返回ID生成器的名字
        return "uuidGenerator";
    }
}
