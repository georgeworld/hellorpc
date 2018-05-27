/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package nio;

import com.github.hellorpc.initializer.Hello;
import com.github.hellorpc.logger.GeorgeLogger;
import com.github.hellorpc.logger.GeorgeLoggerFactory;
import nio.service.TestRPCService;
import org.junit.*;

import java.io.IOException;

/**
 * Hello Server （Netty）服务端测试入口类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class HelloServerTest {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(HelloServerTest.class);
    private Hello hello;

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


    //Hello RPC 服务端启动
    @Test
    public void serverTest() throws IOException {
        //初始化并启动服务端
        try {
            hello = Hello.rpc();
            hello.server().startServer();
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}
