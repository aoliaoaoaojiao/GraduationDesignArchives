#-*- coding: UTF-8 -*-
import json
import requests
import config


def get_boundary_gpsinfo(location='芙蓉区'):
    """
    获取行政区域边界经纬度信息
    Args:
        location:行政区名

    Returns:行政区坐标组

    """
    parameters = {'key': config.key, 'keywords': location, 'filter': config.filterCode, 'subdistrict': 0,
                  'extensions': 'all'}
    base = config.baseUrl
    response = requests.get(base, parameters)
    answer = response.json()
    polyline = answer['districts'][0]["polyline"]  # 字符串类型
    result = []
    for data in polyline.split(';'):
        # 如果‘|’在坐标组中，取第一个数据
        if '|' in data:
            point = data.split('|')[0].split(',')
            result.append([float(point[0]), float(point[1])])
            continue
        point = data.split(',')
        result.append([float(point[0]), float(point[1])])
    return result


if __name__ == '__main__':
    result = {}
    # 获取行政区信息
    with open(config.codeDataFilePath, 'r', encoding='utf-8') as f:
        mess = f.read()

    for data in mess.split('\n'):
        data_list = data.split(",")
        try:
            # 判断是否是北京的行政区
            if data_list[2] == config.cityCode:
                polyline = get_boundary_gpsinfo(location=data_list[1][1:-1])
                # 结构：如 东城区:{"adcode":xxxx,"polyline":xxxxxxx}
                result[data_list[1][1:-1]] = {"adcode": data_list[0], "polyline": polyline}
            else:
                continue
        except Exception as e:
            print(e)
    with open(config.saveJsonPath, 'w', encoding="utf-8") as f:
        # 转存为json数据
        json.dump(result, f)
