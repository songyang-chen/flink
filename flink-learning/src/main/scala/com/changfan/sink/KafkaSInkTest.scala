package com.changfan.sink

import java.util.Properties

import com.changfan.bean.SensorReading
import com.changfan.utils.KafkaUtils
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}

/**
  * author: jiaozhu
  * create: 2020-04-03-11:01
  * tel: 17717876906
  */
object KafkaSInkTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    import org.apache.flink.api.scala._

    val properties: Properties = KafkaUtils.getProperties
    val inputStream = env.addSource(new FlinkKafkaConsumer011[String]("sensor", new SimpleStringSchema(), properties))

    // Transform操作

    val dataStream = inputStream
      .map(
        data => {
          val dataArray = data.split(",")
          SensorReading(dataArray(0).trim, dataArray(1).trim.toLong, dataArray(2).trim.toDouble).toString // 转成String方便序列化输出
        }
      )

    // sink
    dataStream.addSink(new FlinkKafkaProducer011[String]("hadoop132:9092", "sinkTest", new SimpleStringSchema()))
    dataStream.print()

    env.execute("kafka sink test")


  }

}
