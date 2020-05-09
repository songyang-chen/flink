package com.changfan.windowApi

import org.apache.flink.api.common.functions.{AggregateFunction, ReduceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
  * author: jiaozhu
  * create: 2020-04-03-14:25
  * tel: 17717876906
  */
object TestWindow {
  def main(args: Array[String]): Unit = {


    // TODO  API - Window
    val env: StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)


    val socketDS: DataStream[String] = env.socketTextStream("localhost", 9999)
    import org.apache.flink.api.scala._

    val mapDS: DataStream[(String, Int)] = socketDS.map((_, 1))

    // 分流
    val socketKS: KeyedStream[(String, Int), String] = mapDS.keyBy(_._1)

    // TODO 时间窗口 : 以时间作为数据的处理范围
    // 如果多个窗口之间不重合，并且头接尾，尾接头，称之为滚动窗口
    val socketWS: WindowedStream[(String, Int), String, TimeWindow] =
    socketKS.timeWindow(Time.seconds(3))


    val reduceDS: DataStream[(String, Int)] = socketWS.reduce(
      (t1, t2) => {
        (t1._1, t1._2 + t2._2)
      }
    )

    //自定义累加器，聚合
   val value: DataStream[Int] = socketWS.aggregate(new MyAggregateFunction)

    val reduceDS1: DataStream[(String, Int)] = socketWS.reduce(new MyReduceFunction)


    reduceDS.print("window>>>>")

    //触发执行计算
    env.execute()

  }

  //自定义reduce 方法 ，reduce方法第一条数据不做处理，从第二条开始
  //每来一条数据，就会计算一条一次
  //当窗口结束时，直接返回计算结果
  class MyReduceFunction extends ReduceFunction[(String, Int)] {
    override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) = {

      println("reduce=" + value1._2 + value2._2)
      (value1._1, value1._2 + value2._2)
    }


  }


  //自定义累加器函数，继承AggregationFunction
  //定义泛型 输入In,累加器中间处理值 Acc ,输出out
  class MyAggregateFunction extends AggregateFunction[(String, Int), Int, Int] {

    //将输入值对累加器进行更新
    override def add(value: (String, Int), accumulator: Int): Int = {

      accumulator + value._2
    }

    //创建累加器
    override def createAccumulator(): Int = 0

    //获取累加器的值
    override def getResult(accumulator: Int): Int = {
      accumulator
    }

    //合并累加器的值
    override def merge(a: Int, b: Int): Int = {
      a + b
    }
  }

}
