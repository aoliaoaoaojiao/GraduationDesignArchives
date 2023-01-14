package com.alaaaj.sc.tool

import java.util.Properties

/**
*
* 获取配置
 *
 * @author 奥利嗷嗷嗷叫
* @date 2020 /05/23
*/
object GetConfig {

  /**
   * 获取jdbc配置
   *
   * @return { @link Properties}
   */
  def getConfiguredJDBC: Properties = {
    val configJDBC = new Properties
    configJDBC.put("user", "root")
//    configJDBC.put("url", "jdbc:mysql://rm-bp1m2g7006747cs769o.mysql.rds.aliyuncs.com/bigdata?useUnicode=true&characterEncoding=utf8&useSSL=true")
//    configJDBC.put("password", "13788667039Tjccptbtptp")
    configJDBC.put("url", "jdbc:mysql://127.0.0.1/test?useUnicode=true&characterEncoding=utf8")
    configJDBC.put("password", "123456")
    configJDBC.put("driver", "com.mysql.jdbc.Driver")

    configJDBC
  }

  /**
   * 获得spark配置
   *
   * @return { @link Properties}
   */
  def getConfiguredSpark: Properties ={
    val configSpark = new Properties
    configSpark.put("spark.Minutes", "5")
    configSpark.put("spark.TOPNum", "40")
    configSpark
  }

  /**
   * 获得kafka配置
   *
   * @return { @link Properties}
   */
  def getConfiguredKafka: Properties = {
    val configKafka = new Properties
    configKafka.put("bootstrap.servers", "slave1:9092")
//    configKafka.put("bootstrap.servers", "master:9092")
    configKafka.put("group.id", "stream")
    configKafka.put("auto.offset.reset", "earliest")
    configKafka.put("topic", "traffictopic")
    configKafka
  }

}
