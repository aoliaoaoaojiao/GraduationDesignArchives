#-*- coding: UTF-8 -*-
import config as cof
import math


def get_rad(point):
    """
    计算弧度
    :param point: 经度或纬度值
    :return: 返回弧度
    """
    return point * math.pi / 180.0


def get_distance(lon1, lat1, lon2, lat2):
    """
    计算两个经纬度坐标之间的距离
    :param lon1: A点经度
    :param lat1: A点纬度
    :param lon2: B点经度
    :param lat2: B点纬度
    :return: 返回A、B两点之间的距离
    """

    rad_lat1 = get_rad(lat1)
    rad_lat2 = get_rad(lat2)
    # 两点纬度之差
    a = rad_lat1 - rad_lat2
    # 两点经度之差
    b = get_rad(lon1) - get_rad(lon2)

    s = 2 * math.asin(math.sqrt(
        math.pow(math.sin(a / 2), 2) + math.cos(rad_lat1) * math.cos(rad_lat2) * math.pow(math.sin(b / 2), 2)))

    # 单位米
    return s * cof.EARTH_RADIUS


if __name__ == '__main__':
    print(get_distance(113.99063836363636, 22.734819636363635, 114.07036681818182, 22.77712909090909))
