package com.alaaaj.sc.util

/**
 *   道路流畅速度,参考DB11/T 785—2011  北京市地方标准 城市道路交通运行评价指标体系 5.1.2表1
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020 /05/19
 */
object RoadFreeEnum extends Enumeration{
  type Freeway =Double
  type MainRoad = Double
  type SecondaryRoad = Double

  val Freeway = 65.0
  val MainRoad = 40.0
  val SecondaryRoad = 35.0
}
