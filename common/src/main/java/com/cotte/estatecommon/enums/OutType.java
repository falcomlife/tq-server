package com.cotte.estatecommon.enums;

public enum OutType {
    GOOD("良品", 1), POOR("不良", 2), INSTORAGEERR("来料异常", 3), PLATINUM("白金出库", 4), CUSO4("硫酸铜出库", 5),RETURN("返回挑面", 7), OTHER("其他", 6);

    private String name;
    private int index;

    OutType(String name, int index) {
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
        if (index == OutType.GOOD.getIndex()) {
            return OutType.GOOD.getName();
        } else if (index == OutType.POOR.getIndex()) {
            return OutType.POOR.getName();
        } else if (index == OutType.POOR.getIndex()) {
            return OutType.POOR.getName();
        } else if (index == OutType.INSTORAGEERR.getIndex()) {
            return OutType.INSTORAGEERR.getName();
        } else if (index == OutType.PLATINUM.getIndex()) {
            return OutType.PLATINUM.getName();
        } else if (index == OutType.CUSO4.getIndex()) {
            return OutType.CUSO4.getName();
        } else if (index == OutType.RETURN.getIndex()) {
            return OutType.RETURN.getName();
        } else {
            return OutType.OTHER.getName();
        }
    }
}
