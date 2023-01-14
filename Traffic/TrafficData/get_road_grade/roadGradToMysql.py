#-*- coding: UTF-8 -*-
import pymysql
import config
import os
import tqdm


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


def query_level(name):
    """
    从数据库中获取道路等级
    Args:
        name: 道路名

    Returns:道路等级，若没有返回None

    """
    sql_str = ("SELECT level"
               + " FROM streets_level"
               + " WHERE name='%s' "%(name))
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


def insert_data_slevel(name, level):
    """
    插入道路等级数据
    Args:
        name: 道路名
        level: 道路等级

    Returns:

    """
    sql_str = "insert into streets_level(name,level)values (%s,%s)"
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str,(name,level))
    cur.close()
    con.commit()
    con.close()


def get_street_level():
    """
    获取各道路对应等级
    Returns:

    """
    result ={}
    data_tmp =set()
    for i in range(1,6):
        # 获取对应最大等级限制包含的道路信息
        file = config.data_save_file.format(level = i)
        with open(file,'r',encoding='utf-8')as f:
            data = set(f.read().split(','))
        # 求差
        difference_set = data-data_tmp
        for name in difference_set:
            result[name]=i
        data_tmp=data

    for key in tqdm.tqdm(result):
        # 判断数据库是否有该道路对应的等级数据
        if query_level(key) is None:
            insert_data_slevel(key,result[key])


if __name__ == '__main__':
    get_street_level()
    # t = set()
    # tt = set(["3","4","5"])
    # print(tt-t)