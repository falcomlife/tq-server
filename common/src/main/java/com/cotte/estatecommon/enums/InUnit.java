package com.cotte.estatecommon.enums;

public enum InUnit {
    ONE("个", 1), KG("千克", 2), G("克", 3);

    private String name;
    private int index;

    InUnit(String name, int index) {
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
        if (index == InUnit.ONE.getIndex()) {
            return InUnit.ONE.getName();
        } else if (index == InUnit.KG.getIndex()) {
            return InUnit.KG.getName();
        } else if (index == InUnit.G.getIndex()) {
            return InUnit.G.getName();
        } else{
            return null;
        }
    }
}
