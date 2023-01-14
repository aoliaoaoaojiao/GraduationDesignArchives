#-*- coding: UTF-8 -*-
import requests
import config
import json
from threading import Thread
from queue import Queue
import random
import os
import time

points_list = list()


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


def data_extract(json_data):
    """
    对得到的json信息进行解析，将能得到正常信息的数据放入list中
    Args:
        json_data: 得到的json信息

    Returns:
        result：返回解析后的信息列表

    """
    data = json.loads(json_data)
    result = []
    if "trafficinfo" in data.keys():
        roads_mess = data["trafficinfo"]["roads"]
        if len(roads_mess) > 0:
            for mess in roads_mess:
                try:
                    result.append(str(mess['name']))
                except Exception as e:
                    continue
        return result
    else:
        return None

class Spider():
    """
    获取各等级即以上等级道路数据
    """

    def __init__(self,level):
        # url队列
        self.qurl = Queue()
        # 获取的数据
        self.data = list()
        # 线程数
        self.thread_num = config.thread_num
        # 访问失败的坐标点
        self.error_url = Queue()
        self.key = ''
        # 定义爬取道路等级
        self.level = level

    def produce_queue(self):
        """
        生成URL队列
        Returns:
        """
        self.key = random.choice(config.key_list)
        global points_list

        for region_point in points_list:
            # 格式化URL
            url = config.get_rectangle_url.format(point=region_point, key=self.key,level=self.level)
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
        if r.status_code == 200:
            data = data_extract(r.text)
            if data is  not  None:
                self.data.extend(data)
        else:
            self.error_url.put(url)

    def data_save(self):
        """
        数据保存到tmp文件中
        Returns:
        """
        file_path = config.data_save_file.format(level=self.level)
        path = file_path.split("/")[0]
        if not os.path.exists(path):
            os.makedirs(file_path)
        data = ",".join(set(self.data))
        with open(file_path, 'w', encoding='utf-8') as f:
                f.write(data)

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
        Returns:

        """
        self.produce_queue()
        self.thread_spider(self.qurl)
        while self.error_url.qsize() > 0:
            self.thread_spider(self.error_url)
        self.data_save()
        # 清空data列表数据，防止下次爬取的数据带有旧的信息
        self.data.clear()


def start():
    json_get_point()
    # 爬取高德各等级范围道路
    for i in range(1, 6):
        s = Spider(i)
        s.run()

if __name__ == '__main__':
    json_get_point()
    for i in range(1,6):
        s = Spider(i)
        s.run()