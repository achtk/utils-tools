import os
#切面
_list = list(range(0, 11))
print("获取列表", _list)
print("获取前三个", _list[:3])
#列表生成器
print([x * x for x in range(0, 11)])

print("文件列表", os.listdir("E:/"))
print("pid:", os.getpid())