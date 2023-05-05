package com.cotte.estatecommon.exception;

/*
*@description: 统一异常，用于删除的信息正在被使用的情况，返回异常信息。
*@author: sorawingwind
*@date: 2023/2/28
*/
public class DeletingObjectIsUsedException extends Exception  {

    public DeletingObjectIsUsedException() {
        super();
    }
    public DeletingObjectIsUsedException(String s) {
        super(s);
    }
}
