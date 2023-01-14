#-*- coding: UTF-8 -*-
import config
import json
import os
from util import *
from multiprocessing import Pool
from mysqlutil import *


def polyline_split(polyline, flag):
    """
    坐标点切割，分为X轴坐标与Y轴坐标
    Args:
        polyline: 坐标点
        flag: 状态，分别为‘x’,‘y’,用于判断是切割X还是Y轴

    Returns:切割后的坐标点（分X,Y轴）

    """
    i = 1
    if flag == "x":
        i = 0
    if '|' in polyline:
        # 判断是否含有‘|’，有则默认使用‘|’切割后的第一个坐标
        point = polyline.split('|')[0].split(',')
    else:
        # 没有，则正常切割
        point = polyline.split(',')
    return float(point[i])


def get_code(polyline_str):
    """
    获取道路对应的地区代码（射线法）
    Args:
        polyline_str:道路坐标点

    Returns:地区代码，若有多个，‘；’连接

    """
    # 获取每个地区的对应区域坐标点
    with open(config.pointJsonPath,'r',encoding='utf-8') as f:
        area_data = json.loads(f.read(),encoding='utf-8')
    polyline_list = polyline_str.split(';')
    # 代码列表，去重
    adcode=set()
    # 生成X轴坐标点，并与Y轴对应
    polyline_x = list(map(lambda points:polyline_split(points, "x"), polyline_list))
    # 生成Y轴坐标点
    polyline_y = list(map(lambda points: polyline_split(points, "y"), polyline_list))
    """分别获取X,Y轴最大最小值"""
    polyline_max_x, polyline_min_x = max(polyline_x), min(polyline_x)
    polyline_max_y,polyline_min_y = max(polyline_y),min(polyline_y)
    # 循环该街道每个坐标点
    for index in range(0,len(polyline_x)):
        # 循环每个地区，判断该点是否在该区域
        for area_key in area_data:
            # 取出区域对应坐标点
            area_points = area_data[area_key]['polyline']
            # 获取区域X,Y轴坐标点对应列表
            area_x_points = [point[0] for point in area_points]
            area_y_points = [point[1] for point in area_points]
            # 获取区域X,Y轴最大最小值
            area_x_points_max,area_x_points_min = max(area_x_points),min(area_x_points)
            area_y_points_max, area_y_points_min = max(area_y_points), min(area_y_points)
            # 区域坐标完全包裹住街道的情况
            if polyline_max_x<area_x_points_max and polyline_min_x>area_x_points_min \
                    and polyline_max_y<area_y_points_max and polyline_min_y>area_y_points_min:
                return area_data[area_key]['adcode']
            # 如果街道完全穿过了该区域的情况
            if polyline_max_x>area_x_points_max and polyline_min_x<area_x_points_min \
                    and polyline_max_y>area_y_points_max and polyline_min_y<area_y_points_min:
                adcode.add(area_data[area_key]['adcode'])
                continue
            # 其他情况进行射线法判断，判断街道的当前坐标点是否在当前循环的区域
            flag = is_point_in_graphics(polyline_x[index],polyline_y[index],area_x_points,area_y_points)
            # 结果为真则包含
            if flag:
                adcode.add(area_data[area_key]['adcode'])
                # 退出当前坐标判断
                break
    return ";".join(adcode)


def data_reorganization(mess):
    """
    数据坐标格式转换成对应区域代码，主要将坐标点的列替换成对应区域代码
    Args:
        mess: 数据

    Returns:新格式数据

    """
    global try_code
    # 尝试获取区域代码
    try:
        polyline_mess = mess[5]
        # 尝试从数据库中获取对应区域代码
        try_code = query_code(mess[0], mess[2])
        # 若数据库没有，则尝试射线法计算
        if try_code is None:
            try_code = get_code(polyline_mess)
            # 将计算结果放入数据库中
            insert_data_sadcode(mess[0], mess[2], try_code)

    except:
        # 数据库出错则尝试计算
        polyline_mess = mess[5]
        try_code = get_code(polyline_mess)
    finally:
        # 如果有多个区域结果
        if ";" in try_code:
            result =""
            # 一条数据扩展为多条
            for code in try_code.split(";"):
                mess[5]=code
                result+=",".join(mess)+"\n"
            # 返回结果
            return result
        mess[5] = try_code
        mess = ",".join(mess)+ "\n"
        return mess


def data_distinct(datas):
    """
    数据去重
    Args:
        datas:数据

    Returns:

    """
    return set(datas)


def data_save(tmp_path='data_tmp/beijing_1588829466501.tmp', data=[]):
    """
    数据保存
    Args:
        tmp_path:临时文件位置
        data: 数据列表

    Returns:

    """
    time = tmp_path.split('_')[2].split('.')[0]
    save_path = config.data_save_path_format.format(time=time)

    # 保存信息
    with open(save_path,'w+',encoding='utf-8') as f:
        for mess in data:
            if mess.get() is None:
                continue
            mess_list = mess.get().split(",")
            if len(mess_list) < 6:
                continue
            if len(mess_list[5]) < 2:
                continue
            f.write(str(mess.get()))
    # 删除文件
    os.remove(tmp_path)


def data_wash(tmp_path='data_tmp/beijing_1588148205563.tmp'):
    """
    数据清洗
    Args:
        tmp_path:临时文件位置

    Returns:

    """
    # 进程池
    pool = Pool(processes=config.processes_num)
    with open(tmp_path, 'r', encoding='utf-8')as f:
        data_tmp = f.read().split('\n')
    data =[]
    for i in range(0, len(data_tmp), config.processes_num):
        # 如果余下数据比线程数小
        if len(data_tmp[i:]) < config.processes_num:
            for ss in data_tmp[i:]:
                if len(ss) <= 1:
                    continue
                # 按找‘/’切割
                mess = ss.split('/')
                try:
                    data.append(pool.apply_async(data_reorganization, (mess,)))
                except:
                    continue
        else:
            for ss in data_tmp[i:i + config.processes_num]:

                if len(ss) <= 1:
                    continue
                mess = ss.split('/')
                try:
                    data.append(pool.apply_async(data_reorganization, (mess,)))
                except:
                    continue
    pool.close()
    pool.join()
    data_save(tmp_path, data)

