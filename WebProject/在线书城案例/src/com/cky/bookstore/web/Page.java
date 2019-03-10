package com.cky.bookstore.web;

import java.util.List;

public class Page<T> {
    //当前第几页
    private int pageNo;

    //当前页的list
    private List<T> list;

    //每页显示多少条记录
    private int pageSize = 3;

    //共有多少条记录
    private long totalItemNumber;

    //构造器中需要对pageNO进行初始化

    public Page(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNo() {
        if(pageNo<=0){
            pageNo=1;
        }else if(pageNo>getTotalPageNumber()){
            pageNo = getTotalPageNumber();
        }
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    //获取总页数

    public int getTotalPageNumber() {
        int totalPageNumber = (int) (totalItemNumber/pageSize);
        if(totalItemNumber%pageSize!=0){
            totalPageNumber++;
        }
        return totalPageNumber;
    }

    public void setTotalItemNumber(long totalItemNumber) {
        this.totalItemNumber = totalItemNumber;
    }

    public boolean isHasNext(){
        if(getPageNo()<getTotalPageNumber()){
            return true;
        }
        return false;
    }

    public boolean isHasPrev(){
        if(getPageNo()>1){
            return true;
        }
        return false;
    }

    public int getPrevPage(){
        if(isHasPrev()){
            return getPageNo()-1;
        }
        return getPageNo();
    }

    public int getNextPage(){
        if(isHasNext()){
            return getPageNo()+1;
        }
        return getPageNo();
    }
}
