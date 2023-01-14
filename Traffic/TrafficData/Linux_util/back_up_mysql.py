#-*- coding: UTF-8 -*-
import pymysql
import time
import logging
from apscheduler.schedulers.blocking import BlockingScheduler


def get_connect_db():
    conn = pymysql.connect(
        host = "",
        port = 3306,
        user = "root",
        passwd = "",
        db = "bigdata",
        connect_timeout=30,
        charset='utf8',
    )
    return conn


def data_back(table,args):
    sql_str = "insert into "+table+"_back(name,level,specific_information,speed,`timestamp`,adcode,direction,DTP,status,TPI)" \
              "values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str, (args))
    cur.close()
    con.commit()
    con.close()


def run_back_data(table):
    sql_str = ("SELECT *  FROM "+table)
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str)
    rows = cur.fetchall()
    if len(rows) != 0:
        if timestamp_compare(table) is False:
            clear_old(table)
            for key in rows:
                data_back(table,key)
    cur.close()
    con.commit()
    con.close()


def clear_old(table):
    sql_str = ("truncate table "+table+"_back")
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str)
    cur.close()
    con.commit()
    con.close()


def timestamp_compare(table):
    sql_str1 = ("SELECT max(`timestamp`)  FROM " + table)
    sql_str2 = ("SELECT max(`timestamp`)  FROM " + table+"_back")
    con = get_connect_db()
    cur = con.cursor()
    cur.execute(sql_str1)
    rows1 = cur.fetchall()
    cur.execute(sql_str2)
    rows2 = cur.fetchall()
    cur.close()
    con.commit()
    con.close()
    return rows1[0][0] == rows2[0][0]


def run():
    tables = ["citytop","diffareatop","diffdirectiontop","diffroadleveltop","realtime"]
    for table in tables:
        run_back_data(table)


if __name__ == '__main__':
    # run()
    ##日志设置
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',  # 日志内容格式
        datefmt='%Y-%m-%d %H:%M:%S',  # 时间格式
        filename='MySqlBack_log.txt',  # 保存位置
        filemode='a'  # 操作文件模式
    )
    scheduler = BlockingScheduler()
    scheduler.add_job(run, 'interval', minutes=3, start_date="2020-03-23 18:00:00")
    scheduler._logger = logging
    scheduler.start()
