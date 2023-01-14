#-*- coding: UTF-8 -*-
import datetime
import threading
import requests
import time
from threading import Thread
from queue import Queue
import config
from apscheduler.schedulers.blocking import BlockingScheduler
from data_wash import *
import logging
from util import *

# 矩形坐标点信息
points_list = list()
# 道路对应等级
street_to_level = {}


def json_get_point():
    """
    获取矩形坐标
    Returns:
    """
    with open(config.json_path, 'r') as json_file:
        json_data = json.load(json_file)
    global points_list
    # 取出矩形坐标点
    points_list = json_data['points']


def set_levels():
    """
    设置道路对应的等级
    Returns:

    """
    global street_to_level
    street_to_level = query_all_level()


def data_extract(json_data, millis):
    """
    对得到的json信息进行解析，将能得到正常信息的数据放入list中
    Args:
        json_data: 得到的json信息

    Returns:
        result：返回解析后的信息列表

    """
    data = json.loads(json_data)
    result = []
    try:
        roads_mess = data["trafficinfo"]["roads"]
        global street_to_level
        if len(roads_mess) > 0:
            for mess in roads_mess:
                try:
                    polyline = str(polyline_process(mess['polyline']))
                    # 如果路段的坐标点没有，则插入数据库中
                    if query_polyline(mess['name'],mess['direction']) is None:
                        insert_data_dpolyine(mess['name'],mess['direction'],polyline)
                    # 获取道路等级
                    if mess['name'] in street_to_level.keys():
                        level = street_to_level[mess['name']]
                    else:
                        level = 5
                    data = [
                        str(mess['name']),
                        str(level),
                        str(mess['direction']),
                        str(mess['speed']),
                        str(millis),
                        polyline,
                        angle_direction(mess['angle'])
                    ]
                    # 以‘/’连接
                    data = "/".join(data)+"\n"
                    result.append(data)
                except Exception as e:
                    continue
        return result
    except:
        return result


class Spider():

    def __init__(self):
        # url队列
        self.qurl = Queue()
        # 获取的数据
        self.data = list()
        # 线程数
        self.thread_num = config.thread_num
        # 访问失败的坐标点
        self.error_url = Queue()
        self.q = threading.Lock()
        self.key = ''
        # 每次爬虫key使用次数
        self.count = 0

    def produce_queue(self):
        """
        生成URL队列
        Returns:
        """
        self.key = get_max_key()
        global points_list

        for region_point in points_list:
            # 格式化URL
            url = config.get_rectangle_url.format(point=region_point, key=self.key)
            self.qurl.put(url)

    def requests_url(self, url,millis):
        """
        从官方提供的API获取数据
        Args:
            url: api地址
            Returns:
        """
        r = requests.get(
            headers=config.requests_header,
            url=url
        )
        self.q.acquire()
        self.count += 1
        self.q.release()
        if r.status_code == 200:
            mess = data_extract(r.text, millis)
            if len(mess)>1:
                self.data.extend(mess)
        else:
            self.error_url.put(url)

    def data_save_tmp(self):
        """
        数据保存到tmp文件中
        Returns:
        """
        millis = int(round(time.time() * 1000))
        file_path = config.data_save_tmp_path_format.format(time=millis)
        # 数据去重
        data = data_distinct(self.data)

        with open(file_path, 'w+', encoding='utf-8') as f:
            for line in data:
                f.write(line)
        return file_path

    def thread_spider(self, urls):
        """
        多线程爬虫
        Returns:
        """
        # 启动时间
        millis = int(round(time.time() * 1000))
        global points_list
        ths = []
        for city_index in range(0, len(points_list), config.thread_num):
            for _ in range(0, config.thread_num):
                # 当队列长度不小于0时执行
                if urls.qsize() > 0:
                    th = Thread(target=self.requests_url, args=(urls.get(),millis,))
                    # 防止高德API后台并发量超出
                    time.sleep(config.thread_sleep)
                    th.start()
                    ths.append(th)
            else:
                continue
        for th in ths:
            th.join()

    def run(self):
        """
        启动爬虫
        Returns:临时文件地址

        """
        self.produce_queue()
        self.thread_spider(self.qurl)
        while self.error_url.qsize() > 0:
            self.thread_spider(self.error_url)
        file_path=self.data_save_tmp()
        update_ini_key_numberandtime(number=self.count, key=self.key)
        # 清空data列表数据，防止下次爬取的数据带有旧的信息
        self.data.clear()
        # 重置调用次数
        self.count = 0
        return file_path


def start():
    s = Spider()
    file_path = s.run()
    data_wash(file_path)


if __name__ == '__main__':
    # 日志设置
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',  # 日志内容格式
        datefmt='%Y-%m-%d %H:%M:%S',  # 时间格式
        filename='logs/spider_log.txt',  # 保存位置
        filemode='a'  # 操作文件模式
    )
    # 获取矩阵位置点
    json_get_point()
    set_levels()
    scheduler = BlockingScheduler()
    scheduler.add_job(start, 'interval', minutes=config.minutes, start_date=config.start_time)
    scheduler._logger = logging
    scheduler.start()





    # starttime = time.time()
    # start()
    # end = time.time()
    # print(end - starttime)