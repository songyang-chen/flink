package com.changfan.utils

import java.util.Properties

/**
  * author: jiaozhu
  * create: 2020-04-02-17:27
  * tel: 17717876906
  */
object KafkaUtils {
  def getProperties: Properties = {

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop132:9092")
    properties.setProperty("group.id", "consumer-group")

    //    //这里也可以直接通过classof 来获取
    //    properties.setProperty("key.deserializer", classOf[StringDeserializer].getName)
    //    properties.setProperty("value.deserializer", classOf[StringDeserializer].getName)

    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "latest")
    properties

  }


}
