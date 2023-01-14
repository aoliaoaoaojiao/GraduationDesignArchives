#-*- coding: UTF-8 -*-
import logging
from apscheduler.schedulers.blocking import BlockingScheduler
import os,signal


path = "/root/mypython/get_traffic_data/data_save/"


def restart_flume():

    pid = get_Pid()
    try:
        os.kill(int(pid),signal.SIGKILL)
    finally:
        os.system("nohup /root/apache-flume-1.9.0-bin/bin/flume-ng agent --conf " +
                  "conf/ --name agent --conf-file /root/apache-flume-1.9.0-bin/conf/kafka.conf" +
                  " -Dflume.root.logger==INFO,console &")


def get_Pid():
    result = []
    for i in range(0,30):
        data = os.popen("ps -ef|grep java|grep flume|awk '{print $2;}'")
        res = data.read()
        for line in res.splitlines():
            result.append(line)
    maxlabel = max(result, key=result.count)
    return maxlabel


if __name__ == '__main__':
    # 日志设置
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',  # 日志内容格式
        datefmt='%Y-%m-%d %H:%M:%S',  # 时间格式
        filename='monitor_log.txt',  # 保存位置
        filemode='a'  # 操作文件模式
    )
    scheduler = BlockingScheduler()
    scheduler.add_job(restart_flume, 'interval', minutes=10, start_date="2020-03-23 18:18:00")
    scheduler._logger = logging
    scheduler.start()
    # pid = get_Pid()
    # os.kill(int(pid),signal.SIGKILL)
    # os.system("nohup /root/apache-flume-1.9.0-bin/bin/flume-ng agent --conf " +
    #           "conf/ --name agent --conf-file /root/apache-flume-1.9.0-bin/conf/kafka.conf" +
    #           " -Dflume.root.logger==INFO,console &")
