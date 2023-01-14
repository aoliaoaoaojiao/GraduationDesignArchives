package com.alaaaj.sc.util

import org.apache.spark.sql.Column
import org.apache.spark.sql.expressions.WindowSpec
import org.apache.spark.sql.functions._

object InfoEntropyUtil {

  /**
    *   均值方差归一化
    *
    * @param w 用于分片操作
    * @param column 操作的列
    * @return {  @link Column  }
   * */
  def partitionByNormalized(w:WindowSpec,column: Column):Column={

    column - mean(column).over(w) / variance(column).over(w)

  }


  /**
    *   指标比重*ln(指标比重)   求信息熵步骤中数值
    *
    * @param scaler 归一化后的数据行
    * @return {  @link Column  }
   * */
  def partitionByIndexProportion(w:WindowSpec,scaler:Column):Column={
    scaler /sum(scaler).over(w)*log(scaler /sum(scaler).over(w))
  }

  /**
    *
    *   信息熵冗余 = 1-信息熵
    *
    * @param infoEntropy 指数和tmp比例
    * @return double
   * */
  def getInfoEntropyRedundancy(infoEntropy:Double): Double ={
    1-infoEntropy
  }

  /**
    *   得到信息熵
    *
    * @param w 分区用
    * @param indexProportionTmp 比例指数
    * @return {  @link Column  }
   * */
  def getInfoEntropy(w:WindowSpec,indexProportionTmp:Column): Column ={
    -sum(indexProportionTmp).over(w)/log(count(indexProportionTmp).over(w))
  }

  /**
    *   得到权重
    *   w=d / d.sum w为权重 d为某个指标的信息熵冗余
    * @param redundancy 信息熵冗余
    * @param otherRedundancy 其他数值的冗余
    * @return double
   * */
  def getWeights(redundancy:Double,otherRedundancy:Double*): Double ={
      redundancy/otherRedundancy.sum
  }

  /**
    *   得到综合分数
    *   综合分数 = 各归一化后指标*指标权重后 总和
    * @param normaDTP 归一化的dtp
    * @param normaTPI 归一化的tpi
    * @param DTPWight dtp权重
    * @param TPIWight tpi权重
    * @return double
   * */
  def getScore(normaDTP:Double,normaTPI:Double,DTPWight:Double,TPIWight:Double):Double={
    normaDTP*DTPWight+normaTPI*TPIWight
  }

}
