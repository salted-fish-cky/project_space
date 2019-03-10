package com.cky.sparkproject.spark.session

class SortByKey(val clickCount: Long,
                val orderCount: Long,
                val payCount: Long) extends Ordered[SortByKey] with Serializable {

  override def compare(that: SortByKey) = {
    if(clickCount - that.clickCount != 0){
      clickCount - that.clickCount
    }else if(orderCount - that.orderCount != 0){
      orderCount - that.orderCount
    }else if(payCount - that.payCount != 0){
      payCount - that.payCount
    }
    0
  }
}