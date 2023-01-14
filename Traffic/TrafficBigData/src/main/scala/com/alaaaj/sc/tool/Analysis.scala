package com.alaaaj.sc.tool

import com.alaaaj.sc.tool.InfoEntropyDataFrame._
import com.alaaaj.sc.util.GetTrafficInformation._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}

/**
*   相关分析
*
* @author 奥利嗷嗷嗷叫
* @date 2020 /05/19
*/
object Analysis {
  /*自定义UDF函数*/
  private val udf_getDTP = udf((roadLevel: String,speed:Double) => getDTP(roadLevel,speed))

  private val udf_getDTPTrafficStatus = udf((DTP:Double)=>getDTPCorrespondTrafficStatus(DTP))

  private val udf_getTPI = udf((DTP:Double)=>getDTPCorrespondTPI(DTP))

  private val udf_getOverallSituation = udf((score:Double)=>overallTrafficConditions(score))


  /**
  *   得到所有路段状况
  *
  * @param baseDataFrmae 基本数据DataFrmae
  * @param sparkSql sparkSql
  * @param saveTable 保存表
  * @return {  @link Dataset < Row >  } */
  def getAllRoadStatus(baseDataFrmae:DataFrame,sparkSql:SparkSession,saveTable:String=null):DataFrame={

    import sparkSql.implicits._

    //每个路段的交通情况
    val eachRoadSituation = baseDataFrmae
      .withColumn("DTP",udf_getDTP(col("level"),col("speed")))
      .withColumn("status",udf_getDTPTrafficStatus(col("DTP")))
      .withColumn("TPI",udf_getTPI($"DTP"))
    //DATA结构： |name|level|specific_information|speed|timestamp|adcode|direction|DTP|status|TPI|
    // 如果表名不为null和空，则保存数据到MySQL表中
    if (saveTable !=null && !saveTable.isEmpty){
      analysisSaveToMysql(eachRoadSituation,saveTable,SaveMode.Append)
    }

    eachRoadSituation
  }


  /**
  *   每个地区的统计数据
  *
  * @param eachRoadSituation 每个道路情况
  * @param sparkSql sparkSql
  * @param EveryRegionComplexData 保存表1 保存每个区域的交通总体情况
  * @param EveryRegionStatusCount 保存表2 保存每个区域的不同交通情况的对应数量
  */
  def eachRegionOfStatistics(eachRoadSituation:DataFrame,sparkSql:SparkSession,EveryRegionComplexData:String=null,EveryRegionStatusCount:String=null): Unit ={

    import sparkSql.implicits._
    // 按照区域划分
    var w = Window.partitionBy($"timestamp",$"adcode")

    /* 整个区域的交通综合数据 */
    // |timestamp|adcode|areaScore|areaSpeedAVG|areaStatus|
    val regionTrafficIndex = getComprehensiveScore(eachRoadSituation)
      .groupBy($"timestamp",$"adcode")
      .agg(
        avg($"score").as("areaScore"),
        avg($"speed").as("areaSpeedAVG")
      )
      .withColumn("areaStatus",udf_getOverallSituation($"areaScore"))

    w = Window.partitionBy($"timestamp",$"adcode",$"status")
    // 每个地区路段交通情况统计
    val regionRoadStatus = eachRoadSituation
      //DATA:|name|level|specific_information|speed|timestamp|adcode|direction|DTP|status|TPI|countStatus|
      // 求每个地区的不同交通状况路段的数量
      .withColumn("countStatus",count($"status").over(w))
      // 求完后去重
      .dropDuplicates("adcode","status")
      // 删除无用字段
      //DATA:|timestamp|adcode|status|countStatus|
      .drop("name","specific_information","level","direction","speed","DTP","TPI")

    if (EveryRegionComplexData !=null && !EveryRegionComplexData.isEmpty){
      analysisSaveToMysql(regionTrafficIndex,EveryRegionComplexData)
    }

    if (EveryRegionStatusCount !=null && !EveryRegionStatusCount.isEmpty){
      analysisSaveToMysql(regionRoadStatus,EveryRegionStatusCount)
    }
  }


  /**
    *   不同道路统计
    *
    * @param baseDataFrame 基础数据DdataFrame
    * @param sparkSql sparkSql
    * @param DiffRoadStatus 保存表
    * @return {  @link Dataset < Row >  }
   * */
  def diffRoadStatistics(baseDataFrame:DataFrame,sparkSql:SparkSession,DiffRoadStatus:String=null):DataFrame={
    import sparkSql.implicits._
    // 对每个道路进行划分
    val w = Window.partitionBy($"timestamp",$"name")

    // 每个道路平均时速和TPI DTP
    val roadDataFrame = baseDataFrame
      .withColumn("speedAVG",avg("speed").over(w))
      .dropDuplicates("timestamp","name")
      .withColumn("DTP",udf_getDTP($"level",$"speedAVG"))
      .withColumn("TPI",udf_getTPI($"DTP"))

    /* 每个道路交通情况*/

    // |name|level|timestamp|adcode|direction|speedAVG|DTP|TPI|roadStatus|
    val diffRoadConditions = roadDataFrame
      .withColumn("roadStatus",udf_getDTPTrafficStatus($"DTP"))
      .drop("specific_information","speed")


    if (DiffRoadStatus !=null && !DiffRoadStatus.isEmpty){
      analysisSaveToMysql( diffRoadConditions,DiffRoadStatus)
    }
    diffRoadConditions
  }


  /**
    *   不同级别公路统计
    *
    * @param diffRoadConditions 不同路况
    * @param sparkSql sparkSql
    * @param DiffLevelRoadOverall 保存表1
    * @param DiffLevelStatusCount 保存表
    */
  def diffLevelRoadStatistics(diffRoadConditions:DataFrame,sparkSql:SparkSession,DiffLevelRoadOverall:String=null,DiffLevelStatusCount:String=null):Unit={
    import sparkSql.implicits._

    /* 每个等级的道路的总体情况*/
    // |timestamp|level|scoreAVG|diffLevelSpeed|diffLevelRoadStatus|
    val roadOverAllStatus = getComprehensiveScore(diffRoadConditions,$"timestamp",$"level")
      .groupBy($"timestamp",$"level")
      .agg(
        avg($"score").as("scoreAVG"),
        avg($"speedAVG").as("diffLevelSpeed")
      )
      .withColumn("diffLevelRoadStatus",udf_getOverallSituation($"scoreAVG"))


    // 统计每个等级的道路中不同交通情况的数量
    // |name|level|timestamp|adcode|direction|speedAVG|DTP|TPI|roadStatus|
    val w = Window.partitionBy($"timestamp",$"level",$"roadStatus")
    val levelRoadStatus = diffRoadConditions
        .withColumn("diffLevelCountStatus",count($"roadStatus").over(w))
      .dropDuplicates("level","roadStatus")
      .drop("name","adcode","direction","speedAVG","DTP","TPI")
//      .groupBy($"level",$"roadStatus",$"timestamp")
//      .agg(count($"roadStatus").as("diffLevelCountStatus"))

    if (DiffLevelRoadOverall !=null && !DiffLevelRoadOverall.isEmpty){
      analysisSaveToMysql( roadOverAllStatus,DiffLevelRoadOverall)
    }
    //|level|roadStatus|timestamp|diffLevelCountStatus|
    if (DiffLevelStatusCount !=null && !DiffLevelStatusCount.isEmpty){
      analysisSaveToMysql( levelRoadStatus,DiffLevelStatusCount)
    }
  }


  /**
  *   不同方向的统计
  *
  * @param eachRoadSituation 每个道路情况
  * @param sparkSql sparkSql
  * @param DiffDirectionScore 保存表1
  * @param DiffDirectionStatusCount 保存表
  */
  def diffDirectionsOfStatistical(eachRoadSituation:DataFrame,sparkSql:SparkSession,DiffDirectionScore:String=null,DiffDirectionStatusCount:String=null): Unit ={
    import sparkSql.implicits._
    //按方向划分
    // |timestamp|direction|directionScore|directionSpeedAVG|directionStatus|
    var w = Window.partitionBy("timestamp","direction")
    val allDirectionStatus = getComprehensiveScore(eachRoadSituation)
      .groupBy($"timestamp",$"direction")
      .agg(
        avg($"score").as("directionScore"),
        avg($"speed").as("directionSpeedAVG")
      )
      .withColumn("directionStatus",udf_getOverallSituation($"directionScore"))



    w = Window.partitionBy($"timestamp",$"direction",$"status")
    // 各方向交通情况统计
    // |timestamp|direction|status|countStatus|
    val diffDirectionData = eachRoadSituation
      .withColumn("countStatus",count($"status").over(w))
      .dropDuplicates("direction","status")
      .drop("name","specific_information","level","adcode","speed","DTP","TPI","rank")

    if (DiffDirectionScore != null && !DiffDirectionScore.isEmpty){
      analysisSaveToMysql( allDirectionStatus,DiffDirectionScore)
    }
    if (DiffDirectionStatusCount !=null && !DiffDirectionStatusCount.isEmpty){
      analysisSaveToMysql( diffDirectionData ,DiffDirectionStatusCount)
    }

  }

  /**
    *   全市总体情况
    *
    * @param eachRoadSituation 每个道路情况
    * @param sparkSql sparkSql
    */
  def wholeCityAsAWhole(eachRoadSituation:DataFrame,sparkSql:SparkSession,theWholeCityScore:String=null): Unit ={
    import sparkSql.implicits._

    // |timestamp|scoreAVG|OverallSituation|
    val cityAllDATA = getComprehensiveScore(eachRoadSituation,$"timestamp")
      .groupBy($"timestamp")
      .agg(avg($"score").as("scoreAVG"))
      .orderBy($"scoreAVG".desc)
      .withColumn("OverallSituation",udf_getOverallSituation($"scoreAVG"))


    if (theWholeCityScore != null && !theWholeCityScore.isEmpty){
      analysisSaveToMysql( cityAllDATA,theWholeCityScore)
    }
  }

  /**
  *   数据topk
  *
  * @param messDataFrame 需要操作的DataFrame
  * @param sparkSql sparkSql
  * @param groupColumnName 需要分组的字段
  * @param orderColumnName 需要排序的字段
  * @param topN topn
  * @param saveTable 保存表
  */
  def dataTopK(messDataFrame:DataFrame,sparkSql:SparkSession,groupColumnName:Column,orderColumnName:Column,topN:Int,saveTable:String=null): Unit ={

    import sparkSql.implicits._
    // 窗口函数，按照相关字段划分再排序
    val w = Window.partitionBy(groupColumnName).orderBy(orderColumnName)

    val topDataFrame =messDataFrame
      .withColumn("sort_index",row_number.over(w))//新建一列，按照默认按照低到高排序并按序添加序号，这里是高到低
      .where($"sort_index"<=topN)// 筛选top数据
      .drop("sort_index")//删除新增的字段

    if (saveTable !=null && !saveTable.isEmpty){
      analysisSaveToMysql( topDataFrame,saveTable,SaveMode.Append)
    }

  }

  /**
  *   分析保存到mysql
  *
  * @param resultDataFrame 需要保存的DataFrame
  * @param saveTable 保存表
  */
  def analysisSaveToMysql(resultDataFrame:DataFrame,saveTable:String,saveTypes:SaveMode=SaveMode.Append): Unit ={

    val configJDBC = GetConfig.getConfiguredJDBC
    val url = configJDBC.getProperty("url")
    configJDBC.remove("url")
    try {
      resultDataFrame.write.mode(saveTypes).jdbc(url,saveTable,configJDBC)
    }catch {
      case e:Exception=>println(e)
    }
  }
}
