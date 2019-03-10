package com.cky.sparkproject.spark.session;

import scala.math.Ordered;

import java.io.Serializable;

/**
 * 品类的二次排序
 *
 * 封装你要进行排序算法需要的几个字段
 */
public class CatetorySortKey implements Ordered<CatetorySortKey>,Serializable {

    private long clickCount;
    private long orderCount;
    private long payCount;

    public CatetorySortKey(long clickCount, long orderCount, long payCount) {
        this.clickCount = clickCount;
        this.orderCount = orderCount;
        this.payCount = payCount;
    }

    public CatetorySortKey(){

    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public long getPayCount() {
        return payCount;
    }

    public void setPayCount(long payCount) {
        this.payCount = payCount;
    }

    @Override
    public int compare(CatetorySortKey that) {
        return 0;
    }

    @Override
    public boolean $less(CatetorySortKey that) {
        if(clickCount < that.getClickCount()){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount < that.getOrderCount()){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount == that.getOrderCount() &&
                payCount<that.getPayCount()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $greater(CatetorySortKey that) {
        if(clickCount > that.getClickCount()){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount > that.getOrderCount()){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount == that.getOrderCount() &&
                payCount>that.getPayCount()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $less$eq(CatetorySortKey that) {
        if($less(that)){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount == that.getOrderCount() &&
                payCount == that.getPayCount()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $greater$eq(CatetorySortKey that) {
        if($greater(that)){
            return true;
        }else if(clickCount == that.getClickCount() &&
                orderCount == that.getOrderCount() &&
                payCount == that.getPayCount()){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(CatetorySortKey that) {
        if(clickCount - that.getClickCount() != 0){
            return (int) (clickCount - that.getClickCount());
        }else if(orderCount - that.getOrderCount() != 0){
            return (int) (orderCount - that.getOrderCount());
        }else if(payCount - that.getPayCount() != 0){
            return (int) (payCount - that.getPayCount());
        }
        return 0;
    }
}
