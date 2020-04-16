package com.changfan.wordcount

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * author: jiaozhu
  * create: 2020-04-02-16:57
  * tel: 17717876906
  */
object WordCount {
  def main(args: Array[String]): Unit = {

    //创建执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 接收socket文本流
    val textDstream: DataStream[String] = env.socketTextStream("hadoop132", 7777)

    // flatMap和Map需要引用的隐式转换
    import org.apache.flink.api.scala._
    val dataStream: DataStream[(String, Int)] = textDstream.flatMap(_.split("\\s")).filter(_.nonEmpty).map((_, 1)).keyBy(0).sum(1)

    dataStream.print().setParallelism(1)

    // 启动executor，执行任务
    env.execute("Socket stream word count")

  }

}
