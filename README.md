#                                                          rpcDemo
##### 自己写的能够实现远程调用的功能模块Demo
### 条件
#####   本地运行redis，且不需要密码,但是可以自行修改，因为demo基本是在本地运行多个端口的实例而已。
### 服务端
#####   服务信息注册在redis上，包括host:port。服务器可以动态上下线，不会影响客户端调用。服务端向线程池提交
##### 心跳检测和服务监听两个任务，一个负责动态上下线，一个负责接收来自客户端的远程调用。
### 调用端
##### 客户端会随机选择一台服务器进行连接并调用远程方法，如连接不上，会多次重连。
###   实现了通过注解方式注册远程方法
##### 服务端在接口处使用 @IOCService 负责将实现其接口的类实例化，注入容器
##### 客户端在接口处使用 @myRpc 注入远端服务（一定要确保远程有该服务），
##### 调用端会自动生成代理对象，并注入，代理对象负责去向服务器发送调用请求。
##### 普通的注入 在字段上使用 @IOCResource 进行注入
##### 实现的IOC很简单，并不具备扩展功能


