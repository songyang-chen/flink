package com.changfan.sink

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
  * author: jiaozhu
  * create: 2020-04-03-11:30
  * tel: 17717876906
  */
object RedisSinkTest {
  def main(args: Array[String]): Unit = {


    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    streamEnv.setParallelism(1) //默认情况下每个任务的并行度为1

    import org.apache.flink.streaming.api.scala._
    //读取netcat流中数据 （实时流）
    val stream1: DataStream[String] = streamEnv.socketTextStream("hadoop132", 7777)

    //键值对的数据写入kafka
    val stream2: DataStream[(String, String)] = stream1.map(line => {
      val arr: Array[String] = line.split(",")
      (arr(0), arr(1))
    })


    //连接redis的配置
    val config: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder().setDatabase(3).setHost("hadoop132").setPort(6379).build()


    //写入redis
    stream2.addSink(new RedisSink[(String, String)](config, new RedisMapper[(String, String)] {
      override def getCommandDescription = new RedisCommandDescription(RedisCommand.HSET, "t_0615")

      override def getKeyFromData(data: (String, String)) = {
        data._1
      }

      override def getValueFromData(data: (String, String)) = {
        data._2
      }
    }))


    stream2.print("redis sink").setParallelism(1)
    streamEnv.execute()
  }

}
