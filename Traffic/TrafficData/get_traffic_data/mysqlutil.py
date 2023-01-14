#-*- coding: UTF-8 -*-
import time

import pymysql
import config


def get_connect_db():
    """
    连接数据库
    Returns:连接对象

    """
    conn = pymysql.connect(
        host = config.mysqlHost,
        port = config.mysqlPort,
        user = config.mysqlUser,
        passwd = config.mysqlPassword,
        db = config.mysqlData,
        connect_timeout=config.connect_timeout,
        charset='utf8',
    )
    return conn


def query_code(name,diretction):
    """
    从数据库中获取街道对应的区域代码
    Args:
        name: 道路名
        diretction:路段

    Returns:对应区域代码

    """
    sql_str = ("SELECT adcode"
               + " FROM streets_adcode"
               + " WHERE name='%s' and diretction='%s'"%(name,diretction))
    con = get_connect_db()
    cur = con.cursor()

    cur.execute(sql_str)
    rows = cur.fetchall()
    cur.close()
    con.close()
    if len(rows)<=0:
        return None
    else:
        return rows[0][0]


def insert_data_sadcode(name, diretction, code):
    """
    插入路段对应区域数据
    Args:
        name: 道路名
        diretction:路段
        code: 对应区域

    Returns:

    """
    sql_str = "insert into streets_adcode(name,diretction,adcode)values (%s,%s,%s)"
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str,(name,diretction,code))
    cur.close()
    con.commit()
    con.close()


def query_polyline(name,direction):
    """
    查询路段坐标点是否在数据库中
    Args:
        name:道路名
        direction:路段

    Returns:None或坐标组

    """
    sql_str = ("SELECT name"
               + " FROM road_polyline"
               + " WHERE name='%s' and specific_information='%s'" % (name, direction))
    con = get_connect_db()
    cur = con.cursor()

    cur.execute(sql_str)
    rows = cur.fetchall()
    cur.close()
    con.close()
    if len(rows) <= 0:
        return None
    else:
        return rows[0][0]


def insert_data_dpolyine(name,direction,polyline):
    """
    插入路段对应的坐标组
    Args:
        name: 道路名
        direction: 路段
        polyline: 坐标组

    Returns:

    """
    sql_str = "insert into road_polyline(name,specific_information,polyline)values (%s,%s,%s)"
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str, (name, direction, polyline))
    cur.close()
    con.commit()
    con.close()


def query_all_level():
    """
    从数据库中获取道路对应的等级（高德地图划分标准）
    Returns:

    """
    sql_str = ("SELECT name,level"
               + " FROM streets_level"
               )
    con = get_connect_db()
    cur = con.cursor()

    cur.execute(sql_str)
    rows = cur.fetchall()
    cur.close()
    con.close()
    result = {}
    if len(rows)<=0:
        return None
    else:
        for data in rows:
            result[data[0]] = data[1]
        return result

