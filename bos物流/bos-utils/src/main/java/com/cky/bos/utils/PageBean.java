package com.cky.bos.utils;

import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public class PageBean {

    private Integer currentPage; //当前页码
    private Integer pageSize;      //每页显示条数
    private Integer total;      //总条数
    private List rows;   //当前页显示的集合
    private DetachedCriteria detachedCriteria;  //查询条件

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
        this.detachedCriteria = detachedCriteria;
    }

    public DetachedCriteria getDetachedCriteria() {
        return detachedCriteria;
    }
}
