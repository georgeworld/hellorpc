/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package test.server;

import com.github.hellorpc.server.Registry;
import com.github.hellorpc.server.VirtualServer;
import middle.action.ActionApi;
import org.junit.*;
import test.server.impl.ActionImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello RPC 服务端测试入口类
 *
 * @author dev1
 */
public class HelloRPCServer {

    static Integer port = 6000;

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
    public void serverStartup() throws IOException {
        Registry.RegistryHolder.getInstance().regist(new ActionImpl(), ActionApi.class);
        ServerSocket ss = new ServerSocket(6000);
        System.out.println("------------- server started------------------");
        while (true) {
            Socket s = ss.accept();
            new VirtualServer(s).start();
        }
    }
}
