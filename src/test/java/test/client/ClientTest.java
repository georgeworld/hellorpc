/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package test.client;

import com.github.hellorpc.client.OldHelloRPC;
import com.github.hellorpc.container.MapContainer;
import middle.action.ActionApi;
import middle.entities.TestBean;
import org.junit.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello Server 客户端测试入口类
 *
 * @author George <Georgeinfo@163.com>
 */
public class ClientTest {

    public ClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void name() throws Exception {
    }

    // 单元测试方法在下面写
    @Test
    public void hello() throws UnsupportedEncodingException {
        //PHP服务器端口
        int serverPort_PHP = 8081;
        //Java服务器端口
        int serverPort_Java = 6000;

        //这个是连接Hello Server PHP Server
        //OldHelloRPC rpcServer = new OldHelloRPC("127.0.0.1", serverPort_PHP, "php/indexServer.php", false);

        //这个是连接Hello Server Java Server
        OldHelloRPC rpc = new OldHelloRPC("127.0.0.1", serverPort_Java, null, true);

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

//        other.calc3("abc");
//        other.calc7(mc);
//        System.out.println(new String(new ObjectMapper().writeValueAsBytes(a)));
//        other.calc1((TestBean)a1);
//        System.out.println(m.get("a"));
    }
}
