# tree-dict
树形字典仓储服务

# 安装
1.安装 MySQL 5.7+   
2.创建数据库 tree_dict 并设置用户名密码，用户名和密码需要更新到 tree-dict/src/main/resources/application-local.yml 中  
3.执行 tree-dict/db/ 目录下的 SQL   
4.maven 编译项目  
5.启动项目  

# 使用
参考 com.hong.treedict.web.TreeDictWebResource 接口，该接口支持增、删、查，插入场景有分插入根节点和普通节点，普通节点需要定位才能插入，比如给定父节点和左边旁系节点，那么就可以使用 insertPL 方法，同理，给定父节点并且要在此父节点下第一个位置插入就可以使用 insertPH 方法。


