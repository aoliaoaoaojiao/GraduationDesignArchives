package com.alaaaj.sc.tool

import com.alaaaj.sc.util.InfoEntropyUtil._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame}

/**
*   信息熵相关操作
*
* @author 奥利嗷嗷嗷叫
* @date 2020 /05/20
*/
object InfoEntropyDataFrame {
  // 信息熵冗余
  private val udf_getRedundancy = udf((infoEntropy: Double) => getInfoEntropyRedundancy(infoEntropy))
  // 信息权重
  private val udf_getWight = udf(
    (redundancy : Double,redundancyDTP:Double,redundancyTPI:Double) => getWeights(redundancy,redundancyDTP,redundancyTPI))
  // 获取综合评分
  private val udf_getScore = udf(
    (normaDTP:Double,normaTPI:Double,DTPWight:Double,TPIWight:Double)=>
    getScore(normaDTP,normaTPI,DTPWight,TPIWight)
  )


  /**
    *   数据归一化
    *
    * @param dataFrame 数据
    * @param groupColumnName 分组字段
    * @return {  @link Dataset < Row >  }
   * */
  def dataNormalized(dataFrame: DataFrame,groupColumnName:Column*): DataFrame ={


    val w = Window.partitionBy(groupColumnName:_*)
    // DTP归一化
    val DTPScaler = dataFrame.withColumn("scaler_DTP",partitionByNormalized(w,col("DTP")))
    // TPI归一化
    val resultScaler = DTPScaler.withColumn("scaler_TPI",partitionByNormalized(w,col("TPI"))).na.fill(0.0)

    resultScaler
  }

    /**
    *   综合评分
    *
    * @param dataFrame 数据
    * @param groupColumnName 分组名
    */
  def getComprehensiveScore(dataFrame: DataFrame,groupColumnName:Column*): DataFrame ={

    val normalizedDataFrame = dataNormalized(dataFrame,groupColumnName:_*)

    val w = Window.partitionBy(groupColumnName:_*)
    val resultDataFrame = normalizedDataFrame
      // 指标比重*ln(指标比重)
      .withColumn("ProportionDTPTmp",partitionByIndexProportion(w,col("scaler_DTP")))
      .withColumn("ProportionTPITmp",partitionByIndexProportion(w,col("scaler_TPI")))
      .na.fill(0)
      .withColumn("EntropyDTP",getInfoEntropy(w,col("ProportionDTPTmp")))
      .withColumn("EntropyTPI", getInfoEntropy(w,col("ProportionTPITmp")))
      .na.fill(0)
      .withColumn("redundancyDTP",udf_getRedundancy(col("EntropyDTP")))
      .withColumn("redundancyTPI",udf_getRedundancy(col("EntropyTPI")))
      .withColumn("WightDTP",udf_getWight(col("redundancyDTP"),col("redundancyDTP"),col("redundancyTPI")))
      .withColumn("WightTPI",udf_getWight(col("redundancyTPI"),col("redundancyDTP"),col("redundancyTPI")))
      .withColumn("score",
        udf_getScore(
          col("DTP"),
          col("TPI"),
          col("WightDTP"),
          col("WightTPI")
        )
      )
      .drop(
        "redundancyDTP"
        ,"redundancyTPI"
        ,"scaler_DTP"
        ,"scaler_TPI"
        ,"EntropyRedundancyDTP"
        ,"EntropyRedundancyTPI"
        ,"WightTPI"
        ,"WightDTP"
        ,"ProportionDTPTmp"
        ,"ProportionTPITmp"
        ,"EntropyDTP"
        ,"EntropyTPI"
      )

    resultDataFrame
  }
}
