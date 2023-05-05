package com.cotte.estatecommon.utils;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.CollectionUtils;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomCellWeightWeightConfig
 * @description: excel自适应列宽
 * @author: qhs
 * @date: 2023-02-16 14:46
 */
public class CustomCellWeightWeight extends AbstractColumnWidthStyleStrategy {

    private static final int MAX_COLUMN_WIDTH = 255;
    private static final int COLUMN_WIDTH_BASE = 255;
    private final Map<Integer, Map<Integer, Double>> cache = new HashMap<>(8);

    private Integer relativeRowIndex = -1;

    public CustomCellWeightWeight() {
    }

    public CustomCellWeightWeight(Integer relativeRowIndex) {
        //这里是指定从第几行开始自适应。0是第一行，1是第二行，以此类推
        this.relativeRowIndex = relativeRowIndex;
    }


    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (needSetWidth) {
            if(this.relativeRowIndex == -1 || relativeRowIndex >= this.relativeRowIndex){
                Map<Integer, Double> maxColumnWidthMap = cache.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>(16));

                double columnWidth = this.dataLength(cellDataList, cell, isHead);
                if (columnWidth >= 0) {
                    if (columnWidth > MAX_COLUMN_WIDTH) {
                        columnWidth = MAX_COLUMN_WIDTH;
                    }
                    Double maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
                    if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
                        maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
                        writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), (int)(maxColumnWidthMap.get(cell.getColumnIndex())*COLUMN_WIDTH_BASE));
                    }
                }
            }
        }
    }


    private double dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData<?> cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        return getExcelWidth(cellData.getStringValue());
                    case BOOLEAN:
                        return getExcelWidth(cellData.getBooleanValue().toString());
                    case NUMBER:
                        return getExcelWidth(cellData.getNumberValue().toString());
                    default:
                        return -1;
                }
            }
        }
    }

    /**
     * 调整单元格字符字节宽度，easyExcel默认直接用的UTF-8的byte长度，导致一旦三字节的字符过多就会变得很宽，一字节的字符过多就会不够宽
     */
    private double getExcelWidth(String str){
        double length = 0.0;
        char[] chars = str.toCharArray();
        for(char c : chars){
            byte[] bytes = this.getUtf8Bytes(c);
            if(bytes.length == 1){
                length += 1.05;
            }
            if(bytes.length == 2){
                length += 1.5;
            }
            if(bytes.length == 3){
                length += 1.85;
            }
            if(bytes.length == 4){
                length += 2.2;
            }
        }
        return length;
    }

    private byte[] getUtf8Bytes(char c) {
        char[] chars = {c};
        CharBuffer charBuffer = CharBuffer.allocate(chars.length);
        charBuffer.put(chars);
        charBuffer.flip();
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        return byteBuffer.array();
    }
}
