#-*- coding: UTF-8 -*-
import json
import requests
import config as cof
import get_distance as dist


def get_json_range(city):
    """
    获取城市坐标数据
    Args:
        city: 城市

    Returns:

    """
    city_point = {}
    # 行政区域API地址
    url = "https://restapi.amap.com/v3/config/district?key=" + cof.AK + "&keywords=" + city + "&subdistrict=0&extensions=all"

    r = requests.get(headers=cof.header, url=url)

    data = json.loads(r.text)
    # 获取行政范围
    point_str = data['districts'][0]['polyline']
    point_list = str.split(point_str, ";")
    area_cut_list = area_cut(get_extremum(point_list))
    city_point['name'] = city
    city_point['points'] = area_cut_list

    with open("./city_range_cut/" + city + "_cut.json", "w", encoding='utf-8') as f:
        json.dump(city_point, f)
    print(city + "的数据保存成功")


def get_extremum(point_list):
    """
    获取极值
    :param point_list:
    :return:返回该地区行政范围
    """

    # 经度列表
    longitude_list = []
    # 纬度列表
    latitude_list = []
    # 将数据放入列表中
    for point in point_list:
        # 如果字符串中含有"|"，则取最小的那个坐标
        if "|" in point:
            point = str.split(point, "|")[0]

        point_temp = str.split(point, ",")
        longitude_list.append(float(point_temp[0]))
        latitude_list.append(float(point_temp[1]))
    # 最大最小坐标点
    longitude_max = max(longitude_list)
    longitude_min = min(longitude_list)
    latitude_max = max(latitude_list)
    latitude_min = min(latitude_list)
    return longitude_min, latitude_min, longitude_max, latitude_max


def area_cut(point):
    """
    将行政区域切割为接近对角线为10KM的小矩形
    :param point: 行政区域外接矩形
    :return: 切割后的小矩形的经纬度
    """

    points_list = []
    # 得出对角线距离
    distance = dist.get_distance(point[0], point[1], point[2], point[3])
    # 高德地图规定矩形范围为对角线10KM
    if distance % 10000 == 0:
        scale = int(distance / 10000)
    else:
        scale = int(distance / 10000) + 1
    # 当比例小于2时直接返回该行政范围
    if scale < 2:
        return [point]
    point_x = point[2] - point[0]
    point_y = point[3] - point[1]

    scale_x = point_x / scale
    scale_y = point_y / scale
    # 按比例切割
    for i in range(0, scale - 1):
        for j in range(0, scale - 1):
            point_str = judge_area((point[0] + j * scale_x, point[1] + i * scale_y, point[0] + (j + 1) * scale_x,
                                    point[1] + (i + 1) * scale_y))

            if point_str is not None:
                points_list.append(point_str)

    #  返回所有切割后的区域
    return points_list


def judge_area(point):
    """
    判断该区域是否能获取数据
    :param point: 需要验证的矩形区域坐标
    :return: 能获取返回坐标系格式的字符串，不能返回None
    """
    point_str = str(point[0]) + "," + str(point[1]) + ";" + str(point[2]) + "," + str(point[3])
    url = "https://restapi.amap.com/v3/traffic/status/rectangle?rectangle=" + point_str + "&extensions=all&key=" + cof.AK
    try:
        r = requests.get(headers=cof.header, url=url)
    except requests.exceptions.ConnectionError:
        print("错误坐标："+point_str)
        return None
    data = json.loads(r.text)
    # 判断能否获取数据
    if data['status'] == "1" :
        try:
            type(data["trafficinfo"]["roads"][0]['speed'])
            return point_str
        except Exception:
            return None
    else:
        return None


if __name__ == '__main__':
    get_json_range("北京")
