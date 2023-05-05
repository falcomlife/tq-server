package com.sorawingwind.storage.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName 内存分页工具
 * @description:
 * @author: tgt
 * @date: 2022/10/10
 */
public class PaginationUtil<E> {

    // 1) 第一页 1,总记录数大于等于每页记录数 2,每页记录数大于总记录数
    // 2) 其他页 1,分页后的每页记录数等于每页记录数 2,分页后的每页记录数小于于每页记录数
    public List<E> utils(List<E> list,int pageNumber,int pageSize){
        List<E> targetList = new ArrayList();
        if(pageNumber == 1) {
            if (list.size() <= pageSize) {
                return list;
            }else {
                return list.subList(0,pageSize);
            }
        }else if(list.size() < (pageNumber-1) * pageSize - 1 ){
            return null;
        }
        int yu = list.size() % pageSize;
        int shang = 0;
        if(yu > 0){
            shang = (list.size() / pageSize) + 1;
        }else {
            shang =list.size() / pageSize;
        }
        if(pageNumber < shang){
            targetList = list.subList((pageNumber - 1 ) * pageSize, pageNumber*pageSize);
        }
        if(pageNumber == shang){
            targetList = list.subList((pageNumber - 1 ) * pageSize,list.size());
        }
        return targetList;
    }

}
