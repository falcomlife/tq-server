package com.sorawingwind.storage;

public enum OutType {
    NORMAL("正常出库",1),INSTORAGEERR("来料异常",2),WORKLOSS("工作损耗",3),OTHER("其他",4);

    private String name;
    private int index;

    OutType(String name, int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public String toString(){
        return this.name();
    }

    public String getName(){
        return this.name;
    }
    public int getIndex(){
        return this.index;
    }
}
