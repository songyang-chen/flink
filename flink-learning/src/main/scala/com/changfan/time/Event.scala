package com.changfan.time

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

/**
  * author: jiaozhu
  * create: 2020-04-17-10:28
  * tel: 17717876906
  */
object EventTimeTest {
  def main(args: Array[String]): Unit = {


     // TODO  API - Window
        val env: StreamExecutionEnvironment =
            StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)
        // 默认情况下，flink处理数据采用的时间为Processing Time
        // 但是实际业务中一般都采用EventTime
        // 可以通过环境对象设置时间语义
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)


        env.execute()
  }

}
