package com.cotte.estatecommon.exception;

/*
*@description: 统一异常，用于数据做唯一性验证时，发现该条数据已经存在的时候，返回异常信息。
*@author: sorawingwind
*@date: 2023/2/28
*/
public class ObjectExsitException extends Exception  {

    public ObjectExsitException() {
        super();
    }
    public ObjectExsitException(String s) {
        super(s);
    }
}
