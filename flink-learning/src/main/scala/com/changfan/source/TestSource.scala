package com.changfan.source

import com.changfan.bean.SensorReading
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * author: jiaozhu
  * create: 2020-04-02-17:06
  * tel: 17717876906
  */
object TestSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    //隐式转换
    import org.apache.flink.api.scala._
    //  方式一 从集合中读取数据
    val stream: DataStream[SensorReading] = env.fromCollection(List(
      SensorReading("sensor_1", 1547718199, 35.80018327300259),
      SensorReading("sensor_6", 1547718201, 15.402984393403084),
      SensorReading("sensor_7", 1547718202, 6.720945201171228),
      SensorReading("sensor_10", 1547718205, 38.101067604893444)

    ))

    //方式二 从文件中读取

    val stream2: DataStream[String] = env.readTextFile("D:\\workplace\\flink-learning\\src\\main\\resources\\sensor.txt")


    //方式三


    //打印
  //  stream.print().setParallelism(1)
    stream2.print().setParallelism(1)

    env.execute()
  }

}
