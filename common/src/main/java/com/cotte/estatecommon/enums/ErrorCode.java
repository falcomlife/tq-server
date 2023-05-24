package com.cotte.estatecommon.enums;

public enum ErrorCode {
    TOKENEXPIRED("token过期", 100), EMPTYTOKEN("token为空", 101);


    private String name;
    private int index;

    ErrorCode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public static String getNameByIndex(int index) {
        if (index == ErrorCode.TOKENEXPIRED.getIndex()) {
            return ErrorCode.TOKENEXPIRED.getName();
        } else if (index == ErrorCode.EMPTYTOKEN.getIndex()) {
            return ErrorCode.EMPTYTOKEN.getName();
        } else{
            return null;
        }
    }

}
