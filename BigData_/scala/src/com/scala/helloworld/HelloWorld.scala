package com.scala.helloworld

import scala.collection.mutable
import scala.collection.immutable
import scala.collection.mutable._
import scala.collection.immutable._
object HelloWorld {

  //定义一个方法
  def m1(f:(Int,Int) => Int) : Int = {
    //在方法体里面调用函数
    f(3,4)

  }

  //定义一个函数
  val func1 = (x: Int) => x + 10;
  val func2 = (x: Int,y: Int) => x*y

  //第二种方式定义方法
  val func3:(Int,Double) => (Double,Int) = {
    (a,b) => (b,a)
  }

  def main(args: Array[String]): Unit = {

    //val arr = Array(1,2,3);
    //for (a <- arr) println(a)

    //val t = for (i <- 1.to(10)) yield i*10
    //println(t)

    //val t = 1.to(10).map(_*10)
   // println(t)

//    val a2 = Array(1,2,3,4,5,6,7,8,9)
//    val b1 = for (i <- a2 if (i % 2 == 0) ) yield  i
//    val b2  = a2.filter(_%2 == 0)
//    println(b1)
//    println(b2)


//    val a = 10
//    println(1-a)
//    println(1.-(a))

//    val r = m1(func1);
//    println(r)

//    val r = m1(func2)
//    println(r)

//    val arr = Array(1,2,3,4,5,6,7,8,9)
//    val r1 = arr.map(x => x * 5)
//    println(r1.toBuffer)

    //定义一个数组
//    val arr1 = new Array[Int](10);
//    val arr2 = Array[Int](10);
//    println(arr1.toBuffer)
//    println(arr2.toBuffer)

//    //数组的一些常用方法
//    val arr3 = Array(1,3,4,5,2)
//    //求和
//    val r1 = arr3.sum
//    println(r1)
//    //求最大值
//    val r2 = arr3.max
//    println(r2)
//    //排序
//    val r3 = arr3.sorted
//    println(r3.toBuffer)
//    val r4 = arr3.sortBy(x=>x)
//    println(r4.toBuffer)
//    val r5 = arr3.sortWith(_>_)
//    println(r5.toBuffer)



    //变长数组
//    val ab = new ArrayBuffer[Int]();
//    //追加一个元素
//    ab += 1;
//    ab += 2;
//    //追加多个元素
//    ab += (3,4,5,6)
//    //追加一个数据
//    ab ++= Array(7,8)
//    //插入元素  第一个参数是要插入的位置，后面的是要插入的元素
//    ab.insert(0,0)
//    println(ab)


//    //构建映射 Map
    //    val m = Map(("a",1),("b",2))
    //    //修改里面的值
    //    m("b") = 10
    //    //增加一个值
    //    m("k") = 20
    //    m += (("i",12))
    //    val j = m.getOrElse("j",0)
    //    println(j)
    //    println(m)

//    //元组
//    val t = ("spark","hadoop",1);
//    //得到一个元组的数据
//    val r = t._1;
//    //另外一种给元组赋值的方式
//    val t1,(x,y,z) = ("java","scala","haha")
//    println(t1)
//    println(r)

//    //将对偶的集合转化成映射
//    val arr = Array(("tom",88),("jerry",55))
//    //拉链操作  zip命令将多个值绑定在一起
//    val a = Array("a","b","c")
//    val b = Array(1,2,3)
//    val r = a.zip(b)
//    println(r.toBuffer)



//    //集合List的相关操作
//    //创建一个不可变的集合
//    val list1 = List(1,2,3,4)
//
//    //将0插入到lst1的前面生成一个新的List
//    val lst2 = 0 :: list1
//    println(lst2)
//
//    //将一个元素添加到lst1的后面产生一个新的集合
//    val lst3 = list1:+5
//    println(lst3)
//
//    //将2个list合并成一个新的List
//    val list3 = List(4,5,6)
//    val lst4 = list1 ++ list3
//    println(lst4)
//
//    //创建一个可变集合
//    val lb = ListBuffer(1,2,3,4)
//    lb(1) = 200
//    //增加一个元素
//    lb += 5
//    //增加一个元组
//    lb.append(10,20)
//    println(lb)


//    //可变的set
//    val set1 = new mutable.HashSet[Int]()
//    //向HashSet中添加元素
//    set1 += 2
//    //add等价于+=
//    set1.add(1)
//    println(set1)




  }

}
