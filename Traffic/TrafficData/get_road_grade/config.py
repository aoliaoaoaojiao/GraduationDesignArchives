#-*- coding: UTF-8 -*-
key_list = []
# 请求头
requests_header = {'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1', }
# url格式
get_rectangle_url = "https://restapi.amap.com/v3/traffic/status/rectangle?" \
                    "rectangle={point}&" \
                    "extensions=all&key={key}&level={level}"
# 保存文件格式
data_save_file = "RoadLevelMessSave/{level}.data"
# 地点json地址
json_path = "../regions_cut/city_range_cut/北京_cut.json"
# 线程数
thread_num = 40
# 防止过度爬取被检测
thread_sleep = 1

# MySQL用户名
mysqlUser = "root"
# MySQL密码
mysqlPassword = ""
# MySQL地址
mysqlHost = ""
# MySQL数据库名称
mysqlData = "bigdata"
# MySQL端口
mysqlPort = 3306
# 连接最大时间(s)
connect_timeout = 30