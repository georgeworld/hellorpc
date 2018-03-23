# Hello RPC. 你好，远程过程调用框架
![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/img/hellorpc-logo.png)<br>

&nbsp;&nbsp;&nbsp;&nbsp; Hello RPC框架，是[老乔](http://www.georgeinfo.com)在2015年启动的一个小玩具框架，主要目的是学习和锻炼底层数据的处理逻辑。<br>
&nbsp;&nbsp;&nbsp;&nbsp; 你没看错，它的名字就叫做**“Hello RPC”**，因为是个试验性质的小框架，所以名字起得也很随便。<br>
&nbsp;&nbsp;&nbsp;&nbsp; 这是一个“远程过程调用”框架，也就是分为“客户端-->数据和接口定义中间层-->服务端”这样典型的结构，类似Java RMI、FaceBook Thrift和gRPC等，当然，无论性能还是扩展性，都比不上人家那些主流的大框架。<br>
&nbsp;&nbsp;&nbsp;&nbsp; Hello RPC是[老乔](http://www.georgeinfo.com)发起，设计了通讯协议，传输规则、执行逻辑，定义了报文格式，并开发了报文解析相关的二进制处理类，然后朋友WANGRR参与开发了很多具体的实现，然后我又利用空余的时间开发了一些应用，最终完成了一个基础小定稿版本。<br>

# 感想
&nbsp;&nbsp;&nbsp;&nbsp; 本来以为开发一个RPC很简单，所以就花费了一个周末的时间，设计了报文格式、通讯协议等，但是到了真正开发的阶段，才发现工作量很大。原来设想的是所有类型的数据，都通过二进制的方式来从底层开始实现，不过后来因为要跨语言（PHP），导致数据类型的处理变得很复杂，所以就引入了Json机制，不过报文整体还是基于二进制的自定义报文结构。<br>
&nbsp;&nbsp;&nbsp;&nbsp; 这是一个基本功能小定稿的Java版本 RPC项目，它有对应的PHP版本的实现，不过代码还没经过详细的测试，所以还没有开源出来，稍后如果有空闲的时间，跨语言的工作会进行下去，会PHP的实现也开源出来。<br>
&nbsp;&nbsp;&nbsp;&nbsp; **总之**，这是一个学习和做实验用的RPC小框架，大家可以用来学习数据的底层处理，用来学习报文结构的设计，就不要在实际的生产环境中使用了，毕竟它只是一个小玩具，还很不成熟。

# 设计
## 设计文档，在[这里](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/doc/HelloRPC-doc.pdf) 。

## 报文结构设计如下：
![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/img/doc-1.png)<br>  

## 技术实现设计如下：
![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/img/doc-2.png)<br>

## 关键代码文件如下：
![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/img/doc-3.png)<br>

## 测试运行的代码文件如下：
![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/ginkgo/hellorpc/img/doc-4.png)<br>


# 测试代码
## 服务端的测试代码如下：
<pre><code>
    @Test
    public void serverStartup() throws IOException {
        Registry.RegistryHolder.getInstance().regist(new ActionImpl(), ActionApi.class);
        ServerSocket ss = new ServerSocket(6000);
        System.out.println("------------- server started------------------");
        while (true) {
            Socket s = ss.accept();
            new VirtualServer(s).start();
        }
    }
</pre></code>

## 客户端的测试代码如下：
<pre><code>
 @Test
    public void hello() throws UnsupportedEncodingException {
        //PHP服务器端口
        int serverPort_PHP = 8081;
        //Java服务器端口
        int serverPort_Java = 6000;

        //这个是连接Hello RPC PHP Server
        //HelloRPC rpc = new HelloRPC("127.0.0.1", serverPort_PHP, "php/indexServer.php", false);

        //这个是连接Hello RPC Java Server
        HelloRPC rpc = new HelloRPC("127.0.0.1", serverPort_Java, null, true);

        //声明动作接口
        ActionApi client = rpc.getClient(ActionApi.class);

        //声明要传输的数据实体对象
        TestBean subBean = new TestBean();
        subBean.setName("hello");
        TestBean testBean = new TestBean();
        testBean.setName("gl");
        testBean.setNames(subBean);

        //测试传输Map
        Map dataMap = new LinkedHashMap();
        dataMap.put("key", testBean);//将数据实体对象放入map中传输
        MapContainer<String, TestBean> mc = new MapContainer<String, TestBean>(dataMap);

        //将各种结构的数据，传入action接口方法，执行
        Map result = client.calc(dataMap);
        System.out.println("Map以及对象传输，执行结果 result:" + result);

        //测试传输list
        List<String> list = new ArrayList<>();
        list.add("George");
        List<String> resultOfList = client.calc(list);
        System.out.println("List传输结果：result of list:" + resultOfList);

        //测试传输字符串
        //TODO: 传输汉字等多字节字符的字符串工作，还需要完善
        String strContent = new String("How are you你好吗？".getBytes("UTF-8"), "UTF-8");
        String result_of_string = client.calc(strContent);
        System.out.println("测试传输字符串：result_of_string:" + result_of_string);
    }
</pre></code>

# 参与及讨论
  &nbsp;&nbsp;&nbsp;&nbsp;欢迎加入《互联网软件之家》QQ群：[693203950](//shang.qq.com/wpa/qunwpa?idkey=61c4589ea5618ae46d063f94cbd9394de290dd39ef46fca059a4309b8c1d7874)<br>  
  ![image](https://raw.githubusercontent.com/georgeworld/georgeworld.github.com/master/gstudio/res/img/qq_group.png) <br> 
  &nbsp;&nbsp;&nbsp;&nbsp;有问题，可以到[这里](https://github.com/georgeworld/hellorpc/issues)来反馈，欢迎您的参与。

