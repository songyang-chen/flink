package com.changfan.source

import java.util.Properties

import com.changfan.utils.KafkaUtils
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

/**
  * author: jiaozhu
  * create: 2020-04-02-17:18
  * tel: 17717876906
  */
object TestSourceFromKafka {
  def main(args: Array[String]): Unit = {


    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val properties: Properties = KafkaUtils.getProperties
    import org.apache.flink.api.scala._


    val stream3 = env.addSource(new FlinkKafkaConsumer011[String]("sensor", new SimpleStringSchema(), properties))


    stream3.print("stream3").setParallelism(1)

    env.execute("source from kafka")
  }

}
