## 关于chatroom代码的说明

所有代码均存放于**socket**模块下

### 其中客户端对应包名com.djh.client

包内代码功能如下：

- Client类

   		负责创建socket，获取InputStream和OutputStream，login和talk函数定义在其中。login负责登录检测，talk负责聊天过程。

- ClientThread类

   		是对Thread类的继承。

   		由于在talk过程中，input.readline会阻塞当前进程。所以Client类的talk方法会创建ClientThread线程，该线程会一直监听来自服务端的消息并将其打印到控制台。

- ClientTest类

   		在该类的main方法中，会创建Client类的对象，并调用client对象的login和talk方法。

以上三类包含了一个客户端的完整实现过程。

------

- ThreadTest类和Test类

   		主要负责黑盒测试功能。ThreadTest类是对Thread的继承，在其run方法中，可以向OutputStream写入想要测试的String，同时创建ClientThread线程来监听服务端的回应。

   		Test类，会创建多个ThreadTest类的对象来模拟多用户登录过程。所有服务端的回应都将被打印到控制台上。

### 服务器端对应包名com.djh.server

包内代码功能如下：

- SocketServer类

       该类的唯一成员变量为线程安全的**ConcurrentHashMap<String,ServerThread>**，目的是存储当前所有在线的用户，以name为key，其对应的ServerThread为value。

       在该类的main方法中，循环执行serverSocket.accept()，每接收到一个客户端socket的连接请求，就创建一个ServerThread类的对象去处理。

- ServerThread类

       真正负责处理响应。分为login和talk过程。login负责与客户端的登录对接，talk负责与客户端的talk对接。