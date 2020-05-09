package com.changfan.transform

import com.changfan.bean.SensorReading
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}

/**
  * author: jiaozhu
  * create: 2020-04-03-10:25
  * tel: 17717876906
  */
object KeyedReduce {
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val stream: DataStream[String] = env.readTextFile("D:\\workplace\\flink-learning\\src\\main\\resources\\sensor.txt")

    import org.apache.flink.api.scala._

    //切割并转化为样例类
    val DSSensorReading: DataStream[SensorReading] = stream.map(data => {
      val dataArray: Array[String] = data.split(",")

      SensorReading(dataArray(0).trim, dataArray(1).trim.toLong, dataArray(2).trim.toDouble)
    })


    val keyedStream: KeyedStream[SensorReading, String] = DSSensorReading.keyBy(_.id)

    keyedStream.print(" k").setParallelism(2)

   // val stream2: DataStream[SensorReading] = keyedStream.reduce((x, y) => SensorReading(x.id, x.timestamp + 1, y.temperature))

   // stream2.print("reduce ").setParallelism(1)


    env.execute("keyBY and reduce ")
  }

}
