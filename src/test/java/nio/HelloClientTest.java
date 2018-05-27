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
 * Hello Server （Netty）客户端测试入口类
 *
 * @author George (GeorgeWorld@qq.com)
 */
public class HelloClientTest {
    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(HelloClientTest.class);

    static Integer port = 9199;

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


    //Hello RPC 客户端
    @Test
    public void clientTest() throws IOException {

        try {
            Hello.rpc().client().startClient();
        } catch (Exception e) {
            LOG.error(e);
        }

        HelloNettyClient client = new HelloNettyClient();
        TestRPCService service = client.getService(TestRPCService.class);
        String name = service.getName(12);
        LOG.debug("RPC执行结果：" + name);
    }
}
