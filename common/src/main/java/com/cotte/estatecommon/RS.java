package com.cotte.estatecommon;

import com.cotte.estatecommon.enums.ErrorCode;

/**
 * @ClassName RS
 * @description: 后端返回前端的统一实体类
 * @author: ygj
 * @date: 2022-03-28 10:23
 */
public class RS {
    private int s;

    private Object rs;

    private ErrorCode code;

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public Object getRs() {
        return rs;
    }

    public void setRs(Object rs) {
        this.rs = rs;
    }

    public int getCode() {
        return this.code.getIndex();
    }

    public void setCode(ErrorCode code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "Result [s=" + s + ", rs=" + rs + "]";
    }

    public static RS ok(Object o) {
        RS rs = new RS();
        rs.setS(0);
        rs.setRs(o);
        return rs;
    }

    public static RS bad(Object o) {
        RS rs = new RS();
        rs.setS(1);
        rs.setRs(o);
        return rs;
    }

    public static RS exc(Object o) {
        RS rs = new RS();
        rs.setS(-1);
        rs.setRs(o);
        return rs;
    }

    public static RS ok() {
        RS rs = new RS();
        rs.setS(0);
        rs.setRs(null);
        return rs;
    }

    public static RS warn(Object o) {
        RS rs = new RS();
        rs.setS(2);
        rs.setRs(o);
        return rs;
    }

    public static RS red(Object o) {
        RS rs = new RS();
        rs.setS(3);
        rs.setRs(o);
        return rs;
    }

}
