package com.changfan.source

import com.changfan.bean.SensorReading
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import scala.collection.immutable
import scala.util.Random

/**
  * author: jiaozhu
  * create: 2020-04-02-18:12
  * tel: 17717876906
  */
object CustomizeSource {
  def main(args: Array[String]): Unit = {


    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val stream: DataStream[SensorReading] = env.addSource(new Mysensor())


    stream.print("stream").setParallelism(2)
5
    env.execute("customize source")
  }

}

class Mysensor() extends SourceFunction[SensorReading] {

  var running: Boolean = true

  override def cancel(): Unit = {
    running = false

  }

  override def run(ctx: SourceFunction.SourceContext[SensorReading]): Unit = {


    //初始化一个随机数发生器
    val rand = new Random()



    var curTemp: immutable.IndexedSeq[(String, Double)] = 1.to(10).map(i => ("sensor_" + i, 65 + rand.nextGaussian() * 20))

    while (running) {

      // 更新温度值
      curTemp = curTemp.map(
        t => (t._1, t._2 + rand.nextGaussian())
      )
      // 获取当前时间戳
      val curTime = System.currentTimeMillis()

      curTemp.foreach(
        t => ctx.collect(SensorReading(t._1, curTime, t._2))
      )
      Thread.sleep(100)

    }
  }
}
