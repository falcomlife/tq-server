package com.cotte.estatecommon.utils;

/**
 * @ClassName
 * @description:
 * @author: tgt
 * @date: 2022/10/12
 */

public class ShopGoodsNoUtils {

    /**
     * 生成规则设备编号:设备类型+五位编号（从1开始，不够前补0）
     *
     * @param equipmentType
     * 				设备类型
     * @param equipmentNo
     * 				最新设备编号
     * @return
     */
    public static String getCode(String equipmentType, String equipmentNo){
        String newEquipmentNo = equipmentType + "00001";

        if(equipmentNo != null && !equipmentNo.isEmpty()){
            int no = Integer.parseInt(equipmentNo);
            int newEquipment = ++no;
            newEquipmentNo = String.format(equipmentType + "%05d", newEquipment);
        }

        return newEquipmentNo;
    }

}
