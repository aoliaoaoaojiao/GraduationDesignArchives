package com.alaaaj.sc.util

/**
*   获取交通相关信息
*
* @author 奥利嗷嗷嗷叫
* @date 2020 /05/19
*/
object GetTrafficInformation {

  /**
    *   得到DTP对应的交通状态
    *
    * @param DTP 延迟比率
    * @return {  @link String  } 交通情况
   **/
  def getDTPCorrespondTrafficStatus(DTP:Double):String={
    //  参考GB/T 33171-2016 城市交通运行状况评价规范 附录C
    if (DTP<0.3)
      "畅通"
    else if (DTP>=0.3 && DTP<0.5)
      "基本畅通"
    else if (DTP>=0.5 && DTP<0.6)
      "轻度拥堵"
    else if (DTP>=0.6 && DTP<0.7)
      "中度拥堵"
    else
      "严重拥堵"
  }

  /**
    *   获取交通延时比DTP对应交通运行指数TPI
    *
    * @param DTP DTP
    * @return double TPI
   * */
  def getDTPCorrespondTPI(DTP:Double):Double={
    if (DTP<0.3) {
      if (DTP<0) 0.0
      else DTP*(2/0.3)
    }
    else if (DTP>=0.3 && DTP<0.5)
    /*
      参考GB/T 33171-2016 城市交通运行状况评价规范 附录C

      在DTP和TPI对照表中转换关系：为 DTP-对应TPI的范围内最小值得出其在该范围的份额,
      再乘以每份份额对应TPI大小 再加该范围TPI最小值
      */
      (DTP-0.3)*2/(0.5-0.3)+2
    else if (DTP>=0.5 && DTP<0.6)
      (DTP-0.5)*2/(0.6-0.5)+4
    else if (DTP>=0.6 && DTP<0.7)
      (DTP-0.6)*2/(0.7-0.6)+6
    else if (DTP>=0.7&&DTP<1)
      (DTP-0.7)*2/(1-0.7)+8
    else 10.0
  }

  /**
    *   得到交通延时比DTP
    *
    * @param roadLevel 排名
    * @param speed 速度
    * @return double DTP
   * */
  def getDTP(roadLevel:String,speed: Double): Double = {
    // 计算延时比(DTP) = （实际行驶时间-流畅时行驶时间）/实际行驶时间
    // 参考GB/T 33171-2016 城市交通运行状况评价规范 7.2.2.2
    // v 实际速度 v` 流畅时速度
    // DTP = (s/v-s/v`)/(s/v) ==> (s.(1/v-1/v`))/(s.(1/v)) ==> v.(1/v-1/v`) 越大越堵
    roadLevel match {
      case "1"=>
        val freeSpeed = RoadFreeEnum.Freeway
        val DTP = speed*( 1/speed - 1/freeSpeed)
        if (DTP<0)0.0
        else DTP
      case "2" =>
        val freeSpeed = RoadFreeEnum.MainRoad
        val delayRatio = speed*( 1/speed - 1/freeSpeed)
        if (delayRatio<0)0.0
        else delayRatio
      case _ =>
        val fluentSpeed = RoadFreeEnum.SecondaryRoad
        val delayRatio = speed*( 1/speed - 1/fluentSpeed)
        if (delayRatio<0)0.0
        else delayRatio
    }
  }

  /**
    *   转换成国家标准
    *
    * @param roadLevel 水平
    * @return {  @link String  } 国家城市道路网道路划分
   * */
  def convertedToNationalStandard(roadLevel:String):String={
    // 按照 GB 50220-1995 划分的道路等级
    /**
    高德地图道路等级划分
      1：高速（京藏高速）
      2：城市快速路、国道(西三环、103国道)
      3：高速辅路（G6辅路）
      4：主要道路（长安街、三环辅路路）
      5：一般道路（彩和坊路）
      6：无名道路


    GB 50220-1995:
    城市道路网包括快速路、主干路、次干路、支路。各自含义如下:

      快速路是指城市道路中设有中央分隔带，单向设置不少于两条车道，全部采用立体交叉与控制出入，实现交通连续通行的道路；
      主干路是指在城市道路网中连接城市各主要分区，以交通功能为主的道路；
      次干路是指城市道路网中的与主干路结合，以集散交通的功能为主、兼有服务功能的道路；
      支路是指城市道路网中与次干路和居住区、工业区、交通设施等内部道路相连接，解决局部地区服务功能的道路。
     */
    roadLevel match {
      //高速路，城市快速路，国道转换成级别1：快速路
      case "1"=>"1"
      case "2"=>"1"
      //高速辅路转换成级别4：支路
      case "3"=>"4"
      // 主要道路转换成级别2：主干路
      case "4"=>"2"
      //一般道路转换成级别3：次干路
      case "5"=>"3"
      case _ =>"4"
    }
  }


  /**
    *   整体交通情况
    *
    * @param score 整体分数
    * @return {  @link String  } 交通状况
   * */
  def overallTrafficConditions(score:Double):String={
    if (score<=1.9824) "畅通"
    else if(score>1.9824 && score<=3.9639)"基本畅通"
    else if(score>3.9639 && score<=5.9443)"轻度拥堵"
    else if(score>5.9443 && score<=7.9248)"中度拥堵"
    else "严重拥堵"
  }
}
