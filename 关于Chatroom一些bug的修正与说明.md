## 关于Chatroom一些bug的修正与说明

1.关于ServerThread类中，登录过程中，高并发造成的相同usr_name也可以登陆成功的问题。

修改前部分代码如下：

```java

String[]infoSpilt=info.split("\\s+");
        if(clientThread.containsKey(infoSpilt[1])){
        out.println("Name exist, please choose another name.");
        }else{
        out.println("You have logined");
        message.add(info);
        name=infoSpilt[1];
        clientThread.put(name,this);
        broadcast(name+" has logined","");
        break;
        }
        }
        }
```

​ 上述代码中，在并发较高的情况下，例如（有10个线程同时使用usr_name为duan
进行登录，由于此时还没有usr_name为duan的键-值对被存放在hashmp中，所有他们有可能同时进入else代码块。又由于hashmap.put方法不会检测重复key，**
所以所有的duan都将登陆成功，最后一个登录成功的duan对应的ServerThread会覆盖掉前面put进来的内容**。

bug修复为：

```java
            String[]infoSpilt=info.split("\\s+");
        ServerThread thread=clientThread.putIfAbsent(infoSpilt[1],this);
        if(thread==null){
        name=infoSpilt[1];
        out.println("You have logined");
        broadcast(name+" has logined","");
        break;
        }else{
        out.println("Name exist, please choose another name.");
        }
        }
        }
```

​ 使用hashmap的puIfAbsent方法，该方法会检测要放入的key是否有重复，若没有即放入并返回null；若有则不放入并返回key对应的value。可以以此判断是否有冲突。

