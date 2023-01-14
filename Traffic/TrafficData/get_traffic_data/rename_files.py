import os
from tqdm import tqdm
if __name__ == '__main__':
    path ="./data_save/"
    # beijing_1590236505158.data.COMPLETED
    for file in tqdm(os.listdir(path=path)):
        # os.rename(path+file,path+file+".COMPLETED")
        if ".COMPLETED" in file:
            file_mess = file.split(".")
            os.rename(path+file,path+".".join(file_mess[0:2]))