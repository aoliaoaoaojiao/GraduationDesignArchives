package com.alaaaj.sc.util

import scala.collection.mutable.ArrayBuffer


/**
*   时间相关操作
*
* @author 奥利嗷嗷嗷叫
* @date 2020 /05/19
*/
object TimeActionUtil {

  /**
   *   判断批次中时间是否同一个时段
   *
   * @param dateDataArray 日期数据数组
   * @return boolean
   * */
  def isABatchOfTimeSameTime(dateDataArray:ArrayBuffer[Long]):Boolean={
    val dateDataSet = dateDataArray.toSet
    if (dateDataSet.isEmpty)false
    else if (dateDataSet.size>1)false
    else true
  }

  /**
    *   转换时间在规定时间内
    *
    * @param timeStamp 时间戳
    */
  def conversionTime(timeStamp:String): Long ={
    val time = timeStamp.toLong
    val newTime = time
//    -time%(1000*60*10)
    newTime
  }
}
