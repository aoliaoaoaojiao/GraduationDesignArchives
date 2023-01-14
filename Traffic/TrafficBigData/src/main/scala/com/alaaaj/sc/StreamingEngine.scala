package com.alaaaj.sc

import java.io.File

import com.alaaaj.sc.tool.Analysis._
import com.alaaaj.sc.tool.{GetConfig, KafkaParams}
import com.alaaaj.sc.util.GetTrafficInformation._
import com.alaaaj.sc.util.TimeActionUtil._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Minutes, StreamingContext}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


/**
 * 流处理
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020 /05/19
 */
object StreamingEngine {
  // 获取spark相关配置
  private val config = GetConfig.getConfiguredSpark

  // 创建StreamingContext对象
  private var ssc: StreamingContext = _

  private var stream: InputDStream[ConsumerRecord[String, String]] = _

  private var offsetRanges: Array[OffsetRange] = _


  /**
   * 得到kafka实时流
   * data结构： 道路名，道路等级(国家标准)，路段信息，道路速度，时间戳，道路所在区域编号，路段方向
   *
   * @return {  @link DStream < Tuple7 < String, String, String, Object, Object, String, String > >  }
   **/
  def getKafkaDStream: DStream[(String, String, String, Double, Long, String, String)] = {

    val kafkaParams = KafkaParams.getKafkaParams

    val topics = KafkaParams.getKafkaTopic //消费主题
    // 从Kafka获取数据
    stream = KafkaUtils.createDirectStream[String, String](

      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    )

    stream.foreachRDD(
      data => {
        offsetRanges = data.asInstanceOf[HasOffsetRanges].offsetRanges
      }
    )

    // 取value值，并按照‘，’切分
    val valueRDD = stream
      .map(data => data.value())
      .map(_.split(","))
      .filter(!_.contains(""))
      // data结构： 道路名，道路等级(国家标准)，路段信息，道路速度，时间戳，道路所在区域编号，路段方向
      .map(data => (data(0), convertedToNationalStandard(data(1)), data(2), data(3).toDouble, conversionTime(data(4)), data(5), data(6)))
    valueRDD

  }

  /**
   * 实时数据分析
   *
   * @param dataRDD RDD数据
   */
  def realTimeRDDAnalysis(dataRDD: RDD[(String, String, String, Double, Long, String, String)]): Unit = {
    // 创建一个sparkSql对象
    val sparkSql = SparkSessionSingleton.getInstance(dataRDD.sparkContext.getConf)
    // 隐式转换
    import sparkSql.implicits._
    // 获取基础DataFrame数据
    val basedDataFrame = dataRDD.toDF("name", "level", "specific_information", "speed", "timestamp", "adcode", "direction")
    // 获取所有路段交通情况信息
    val eachRoadSituation = getAllRoadStatus(basedDataFrame, sparkSql, "RealTimeSection")
    // 北京市总体信息
    wholeCityAsAWhole(eachRoadSituation, sparkSql, "theWholeCityScore")
    //每个区域的交通情况
    eachRegionOfStatistics(eachRoadSituation, sparkSql, "EveryRegionComplexData", "EveryRegionStatusCount")
    //不同方向的交通情况
    diffDirectionsOfStatistical(eachRoadSituation, sparkSql, "DiffDirectionScore", "DiffDirectionStatusCount")

    //不同道路的整体交通情况
    val diffRoadConditions = diffRoadStatistics(basedDataFrame, sparkSql, "DiffRoadStatus")
    //不同等级道路情况
    diffLevelRoadStatistics(diffRoadConditions, sparkSql, "DiffLevelRoadOverall", "DiffLevelStatusCount")

    val TopNum = config.getProperty("spark.TOPNum").toInt
    // 每个区域拥堵前40
    dataTopK(eachRoadSituation, sparkSql, $"adcode", $"TPI".desc, TopNum, "DiffAreaTOP")
    // 按照道路等级分类求TOP40
    //|name|level|specific_information|speed|timestamp|adcode|direction|DTP|status|TPI|
    dataTopK(eachRoadSituation, sparkSql, $"level", $"TPI".desc, TopNum, "DiffRoadLevelTOP")
    // 按照方向求TOP40
    dataTopK(eachRoadSituation, sparkSql, $"direction", $"TPI".desc, TopNum, "DiffDirectionTOP")
    // 全市拥堵前40
    dataTopK(eachRoadSituation, sparkSql, $"timestamp", $"TPI".desc, TopNum, "CityTOP")
  }

  /**
   * 非实时数据分析
   *
   * @param dataRDD RDD数据
   */
  def obsoleteRDDAnalysis(dataRDD: RDD[(String, String, String, Double, Long, String, String)]): Unit = {
    // 创建一个sparkSql对象
    val sparkSql = SparkSessionSingleton.getInstance(dataRDD.sparkContext.getConf)
    // 隐式转换
    import sparkSql.implicits._
    // 获取基础DataFrame数据
    val basedDataFrame = dataRDD
      .toDF("name", "level", "specific_information", "speed", "timestamp", "adcode", "direction")
    // 获取所有路段交通情况信息
    val eachRoadSituation = getAllRoadStatus(basedDataFrame, sparkSql)
    // 北京市总体信息
    wholeCityAsAWhole(eachRoadSituation, sparkSql, "theWholeCityScore")
    //每个区域的交通情况
    eachRegionOfStatistics(eachRoadSituation, sparkSql, "EveryRegionComplexData", "EveryRegionStatusCount")
    //不同方向的交通情况
    diffDirectionsOfStatistical(eachRoadSituation, sparkSql, "DiffDirectionScore", "DiffDirectionStatusCount")
    //不同道路的整体交通情况
    val diffRoadConditions = diffRoadStatistics(basedDataFrame, sparkSql, "DiffRoadStatus")
    //不同等级道路情况
    diffLevelRoadStatistics(diffRoadConditions, sparkSql, "DiffLevelRoadOverall", "DiffLevelStatusCount")
  }

  /**
   * 运行交通分析
   *
   */
  def run(): Unit = {
    val trafficRDD = getKafkaDStream
    trafficRDD.foreachRDD(
      dataRDD => {
        val dataArray = dataRDD.collect()
        //获取总长度
        val num = dataArray.length

        val dateMessArray: ArrayBuffer[Long] = new ArrayBuffer[Long]()
        // 取20个数据
        val size = num / 20
        if (size > 0) {
          for (index <- Range(0, num, size)) {
            // 将时间戳放入数组，用于判断是否同一批次
            dateMessArray.append(dataArray(index)._5)
          }
          // 同一批次时的操作
          if (isABatchOfTimeSameTime(dateMessArray)) {
            realTimeRDDAnalysis(dataRDD)
          }
          /*非实时数据按时间分组*/
          else {
            // 分组
            val groupDataArray = dataArray.groupBy(_._5)
            groupDataArray.foreach(data => {
              // 转换成RDD
              val rdd = ssc.sparkContext.parallelize(data._2)
              obsoleteRDDAnalysis(rdd)
            }
            )
          }
        }
      }
    )
  }

  // 单例模式创建一个SparkSession对象
  object SparkSessionSingleton {
    @transient private var instance: SparkSession = _

    def getInstance(sparkConf: SparkConf): SparkSession = {
      if (instance == null) {
        instance = SparkSession
          .builder
          .config(sparkConf)
          .getOrCreate()
      }
      instance
    }
  }
  // ---- 模拟用
  def subdirs2(dir: File): Iterator[File] = {
    val d = dir.listFiles.filter(_.isDirectory)
    val f = dir.listFiles.filter(_.isFile).toIterator
    f ++ d.toIterator.flatMap(subdirs2 _)
  }
     /**
   *   可用的数据
   *
   * @param sc sc
   * @return {  @link RDD < String[ ][ ] >  }
   * */
  def availableData(sc:SparkContext,path:String): Array[Array[String]] ={
    val originalData = sc.textFile(path).map(_.split("\n"))
    originalData.collect()
  }
  // ---- 模拟结束

  def main(args: Array[String]): Unit = {
//    val sparkContext = new SparkConf()
//      .setAppName("trafficAnalysis")
//      .setMaster("spark://master:7077")
//      .set("spark.streaming.kafka.maxRatePerPartition", "4000")

//    ssc = new StreamingContext(sparkContext, Minutes(config.getProperty("spark.Minutes").toLong))

//    run()
//    ssc.start()
//    ssc.awaitTermination()

    // --模拟用
    val flies = subdirs2(new File("F:\\pycut\\result\\"))

    val sparkConf = new SparkConf().setAppName("sparkOffline").setMaster("local[5]")

    val sc = new SparkContext(sparkConf)

    flies.foreach(flie_path=> {
      val availData = availableData(sc, flie_path.toString)

      val dataRuselt = availData.flatten

        .map(_.split(","))
        // data结构： 道路名，道路等级(国家标准)，路段信息，道路速度，时间戳，道路所在区域编号，路段方向
        .map(data => (data(0), convertedToNationalStandard(data(1)), data(2), data(3).toDouble, conversionTime(data(4)), data(5), data(6)))

      val rdd = sc.parallelize(dataRuselt)
      realTimeRDDAnalysis(rdd)
    }
    )
    //模拟结束

  }
}