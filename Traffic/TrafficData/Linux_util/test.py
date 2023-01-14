import time


def input_data():
    def fib(n):
        if n == 1 or n == 2:
            return 1
        return fib(n - 1) + fib(n - 2)

    print(fib(7))


def run_time(arg):
    def print_run_time(func):
        def wrapper(*args, **kw):
            local_time = time.time()
            func(*args, **kw)
            func_time = time.time() - local_time
            print("函数名：" + func.__name__ + "运行时间：" + func_time)

        return wrapper


if __name__ == '__main__':
    # input_data()
    hex_value = input()
    bit_value = int(hex_value, 16)
    print(bit_value)
    cur = 1
    index = 0
    res = []
    while (cur <= bit_value):
        if cur & bit_value != 0:
            res.append(index)
        cur = cur << 1
        print(bin(cur))
        index += 1
    print(res)
