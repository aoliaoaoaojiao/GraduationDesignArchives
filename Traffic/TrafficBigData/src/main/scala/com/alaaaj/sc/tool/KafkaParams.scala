package com.alaaaj.sc.tool

import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.mutable

/**
*   kafka params
*
* @author 奥利嗷嗷嗷叫
* @date 2020 /05/19
*/
object KafkaParams {
  // 获取kafka相关配置信息
  private val kafkaConfig = GetConfig.getConfiguredKafka

  /**
    *   得到kafka参数
    *
    * @return {  @link Map < String, Object >  } kafka参数
   * */
  def getKafkaParams:mutable.Map[String,Object]={

    mutable.Map[String, Object](
      "bootstrap.servers" -> kafkaConfig.getProperty("bootstrap.servers"), //kafka集群地址
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> kafkaConfig.getProperty("group.id"), //消费者组名
      "auto.offset.reset" -> kafkaConfig.getProperty("auto.offset.reset"), //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
      "enable.auto.commit" -> (true: java.lang.Boolean)) //如果是true，则这个消费者的偏移量会在后台自动提交
  }

  /**
    *   获取kafka消费主题
    *
    * @return {  @link String[ ]  }
   * */
  def getKafkaTopic:Array[String]={
    // 进行消费的主题
    Array(kafkaConfig.getProperty("topic"))
  }

}
