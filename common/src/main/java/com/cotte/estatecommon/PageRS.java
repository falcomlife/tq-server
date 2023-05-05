package com.cotte.estatecommon;

import io.ebean.PagedList;

import java.util.List;

public class PageRS<T> {
    private int pageSize = 100;
    private int pageIndex = 1;
    private int totalPageCount = 0;
    private int totalRowCount = 0;
    private List<T> list = null;

    public PageRS() {
        super();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> PageRS create(PagedList<T> page){
        return new PageRS(page.getPageSize(),page.getPageIndex(), page.getTotalCount(), page.getTotalPageCount(), page.getList());
    }

    public PageRS(int pageSize, int pageIndex, int totalRowCount, int totalPageCount, List<T> list) {
        this.pageIndex = pageIndex;
        this.totalRowCount = totalRowCount;
        this.totalPageCount = totalPageCount;
        this.pageSize = pageSize;
        this.list = list;
    }

    public static <T> PageRS<T> ok(int page, int pageSize, int totalRow, int totalPage, List<T> list) {
        PageRS<T> rs = new PageRS<T>();
        rs.setPageIndex(page);
        rs.setPageSize(pageSize);
        rs.setTotalRowCount(totalRow);
        rs.setTotalPageCount(totalPage);
        rs.setList(list);
        return rs;
    }

    public static <T> PageRS<T> bad(){
        PageRS<T> rs = new PageRS<T>();
        rs.setPageIndex(0);
        rs.setPageSize(0);
        rs.setTotalRowCount(0);
        rs.setTotalPageCount(0);
        rs.setList(null);
        return rs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}