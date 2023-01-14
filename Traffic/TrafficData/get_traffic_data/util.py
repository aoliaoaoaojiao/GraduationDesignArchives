#-*- coding: UTF-8 -*-
import config
import configparser as ini_conf
import time


def get_max_key():
    """
    获取剩余调用次数最多的key，如果从INI文件中获取时间失败则会初始化INI文件
    Returns:剩余调用次数最多的key

    """
    cf = ini_conf.ConfigParser()
    cf.read(filenames=config.key_ini, encoding='utf-8')
    try:
        if not compare_day():
            new_day_key_init(cf)
    except Exception as e:
        new_day_key_init(cf)

    key_list = cf.options('key_list')
    temp = 0
    result_key = ''
    for key in key_list:

        key_count = int(cf.get('key_list', key))
        if key_count > temp:
            temp = key_count
            result_key = key
    return result_key


def update_ini_key_numberandtime(key, number):
    """
    更新INI文件里的调用次数和时间
    Args:
        key: 修改的key
        number: 本次爬虫已用的调用次数

    Returns:

    """
    cf = ini_conf.ConfigParser()
    cf.read(filenames=config.key_ini, encoding='utf-8')
    key_count = int(cf.get('key_list', key))
    set_count = str(key_count - number)
    cf.set('key_list', key, set_count)
    cf.write(open(config.key_ini, 'r+', encoding='utf-8'))
    set_ini_time(cf)


def set_ini_time(cf):
    """
    设置ini文件时间
    Args:
        cf: ConfigParser对象
    Returns:

    """
    set_time = int(round(time.time() * 1000))
    try:
        cf.set('time', 'use_time', str(set_time))

        cf.write(open(config.key_ini, 'r+', encoding='utf-8'))
    except Exception:
        cf.add_section('time')
        cf.set('time', 'use_time', str(set_time))

        cf.write(open(config.key_ini, 'r+', encoding='utf-8'))


def time_transform_format(time_num):
    """
    时间戳转换格式
    Args:
        time_num:时间戳

    Returns:转换后的时间格式字符串

    """
    time_tmp = float(time_num / 1000)
    time_array = time.localtime(time_tmp)
    other_style_time = time.strftime("%Y-%m-%d %H:%M:%S", time_array)
    return other_style_time


def new_day_key_init(cf):
    """
    新的一天初始化时间和调用次数(每天key可调用次数为30000)
    Args:
        cf:ConfigParser对象

    Returns:

    """
    key_list = cf.options('key_list')
    # 重新设置key次数
    for key in key_list:
        cf.set('key_list', key, '30000')
    cf.write(open(config.key_ini, 'r+', encoding='utf-8'))
    # 设置INI文件中的use_time时间
    set_ini_time(cf)


def compare_day():
    """
    比较INI文件中的调用时间判断是否同一天
    Returns:true and false
    """
    cf = ini_conf.ConfigParser()
    cf.read(filenames=config.key_ini, encoding='utf-8')
    old_time = int(cf.get('time', 'use_time'))
    old_time_format = time_transform_format(old_time).split(' ')
    now_time = int(round(time.time() * 1000))
    now_time_format = time_transform_format(now_time).split(' ')
    return now_time_format[0] == old_time_format[0]


def is_point_in_graphics(x, y, x_points, y_points):
    """
    点射法判断坐标是否在区域内
    Args:
        x: 坐标点x轴位置
        y: 坐标点Y轴位置
        x_points: 区域x轴坐标集合
        y_points: 区域Y轴坐标集合

    Returns:bool

    """
    x_min, x_max = min(x_points), max(x_points)
    y_min, y_max = min(y_points), max(y_points)
    flag = False
    # 如果坐标点不在该区域范围，则直接返回false
    if x < x_min or x > x_max or y < y_min or y > y_max:
        return flag
    n = len(x_points)
    for i in range(n):
        j = n - 1 if i == 0 else i - 1  # 防止重合，选择j为数组下标前一个（循环数组）

        # 坐标点y在相邻两点Y轴间，按平行线规则判断（按顺序来，其只能在一边）
        if (y_points[j] > y and y_points[i] < y ) \
                and (x < (x_points[j] - x_points[i]) * (y - y_points[i]) / (y_points[j] - y_points[i]) + x_points[i]):
            flag = not flag
    return flag


def angle_direction(angle):
    """
    将角度变换成方向（"西->东"，"南->北"，"东->西"，"北->南"）
    Args:
        angle: 角度

    Returns:方向

    """
    angle = int(angle)
    if angle < 180:
        count = angle / 45
        if count < 1:
            return "西->东"
        elif count < 3:
            return "南->北"
        else:
            return "东->西"
    else:
        angle = angle - 180
        count = angle / 45
        if count < 1:
            return "东->西"
        elif count < 3:
            return "北->南"
        else:
            return "西->东"


def polyline_process(polyline):
    """
    处理坐标点
    Args:
        polyline:坐标点

    Returns:返回以‘;’分隔的坐标点

    """
    result = []
    for data in polyline.split(";"):
        if "|" in data:
            # 如果遇到以‘|’分隔的坐标点，则取第一个坐标
            result.append(data.split("|")[0])
            continue
        result.append(data)
    return ";".join(result)


