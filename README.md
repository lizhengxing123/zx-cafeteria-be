# 正星饭堂后端代码

## 文件目录

- aspect：切面，包含公共字段自动填充
- config：配置类，用于配置项目的一些参数
- constant：常量类，用于定义项目的一些常量
- controller：控制器，admin端和user端的请求都在这里处理
- dto：数据传输对象，用于定义前端给后端传递的数据模型
- entity：实体类，用于映射数据库表
- exception：异常类，用于处理业务异常
- filter：过滤器，包含解密、校验token等功能
- handler：异常处理器，用于处理Controller层抛出的异常
- json：json工具类，用于处理json数据
- mapper：数据库操作接口，用于与数据库交互
- properties：配置文件，用于定义项目的一些参数
- result：结果类，用于定义Controller层返回的结果模型
- service：服务层，用于处理业务逻辑
- utils：工具类，用于封装常用的方法
- vo：视图对象，用于定义后端给前端传递的数据模型
- ZxCafeteriaApplication.java：应用程序入口类
- resources.mapper：mapper xml文件，用于定义数据库操作的sql语句
- application.yml：配置文件，用于定义项目的一些参数
- README.md：项目说明文档
