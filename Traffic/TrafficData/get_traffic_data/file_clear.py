import os
from tqdm import tqdm
import mysqlutil


def file_read(file):
    """
    读取文件数据
    Args:
        file: 文件位置

    Returns:

    """
    with open(file,"r",encoding="utf-8")as f:
        data = f.read()
    return data.split("\n")


def data_clear(data):
    """
    数据清理
    Args:
        data:数据

    Returns:

    """
    results = []
    level_data = mysqlutil.query_all_level()
    for line in data:
        if len(line)<2:
            continue
        mess = line.split(",")
        if ";" in line:
            codes = mess[5].split(";")
            for code in codes:
                mess[5] = code
                mess[1] = level_data[mess[0]]
                result = ",".join(mess)
                results.append(result)
            continue
        mess[1]=level_data[mess[0]]

        results.append(",".join(mess))
    return results


def save_data(file,results):
    """
    保存数据
    Args:
        file:
        results:

    Returns:

    """
    os.remove(file)
    with open(file,"w+",encoding="utf-8") as f:
        for result in results:
            f.write(result+"\n")


if __name__ == '__main__':
    path = "data_save/"
    files = os.listdir(path)
    # beijing_1588393866509
    for file in tqdm(files):
        data = file_read(path+file)
        results = data_clear(data)
        save_data(path+file,results)