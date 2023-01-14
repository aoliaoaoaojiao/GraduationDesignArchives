#-*- coding: UTF-8 -*-
# 线程数
thread_num = 50
# 线程休眠时间
thread_sleep = 0.3
# url格式
get_rectangle_url = "https://restapi.amap.com/v3/traffic/status/rectangle?" \
                    "rectangle={point}&" \
                    "extensions=all&key={key}"
# 地点json地址
json_path = "../regions_cut/city_range_cut/北京_cut.json"
# 请求头
requests_header = {'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1', }
# 数据临时保存文件路径格式
data_save_tmp_path_format = 'data_tmp/beijing_{time}.tmp'
# 数据清洗后保存位置格式
data_save_path_format = 'data_save/beijing_{time}.data'
# 进程数
processes_num = 40
# ini文件位置
key_ini = 'mykey.ini'
# 启动时间
start_time = '2020-03-23 18:00:00'
# 启动间隔时间(分钟)
minutes = 5
# 行政区域坐标点的json文件地址
pointJsonPath = "../areas_polylines/data/areas_mess.json"
# MySQL用户名
mysqlUser = "root"
# MySQL密码
# mysqlPassword = ""
mysqlPassword = "123456"
# MySQL地址
# mysqlHost = ""
mysqlHost = "127.0.0.1"
# MySQL数据库名称
mysqlData = "bigdata"
# MySQL端口
mysqlPort = 3306
# 连接最大时间(s)
connect_timeout = 30
