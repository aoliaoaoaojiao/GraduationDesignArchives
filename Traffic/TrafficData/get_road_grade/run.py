#-*- coding: UTF-8 -*-
import getRoadLevelData
import roadGradToMysql


def run():
    getRoadLevelData.start()
    roadGradToMysql.get_street_level()


if __name__ == '__main__':
    run()