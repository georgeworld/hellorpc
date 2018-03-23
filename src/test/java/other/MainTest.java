/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package other;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 非HelloRPC功能的测试类
 *
 * @author George <Georgeinfo@163.com>
 */
public class MainTest {

    public MainTest() {
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
    public void hello() {
        Class clazz = java.io.BufferedInputStream.class;
        System.out.println(clazz.getName().replace("." + clazz.getSimpleName(), ""));
//        ArrayList<Object> params = new ArrayList<Object>();
//        params.add(1);
//        params.add("a");
//        MsgBuilder builder = MsgBuilder.begin()
//                .buildTpdu().addTpduFrom("0001").addTpduTo("0002").done()
//                .buildTimestamp(System.currentTimeMillis())
//                .buildMti().addMti("0001").done()
//                .buildDefinitedFields().addDefinitedField0(System.currentTimeMillis()).done()
//                .buildMsgContent().addActionClassName("A").addMethodName("abc").addPackagePath("com.demo").addMethodParamsList(params).done()
//                .buildHeader().addVersion(Short.valueOf("100")).addCompressionFlag(false).addEncryptionFlag(false)
//                              .addActionCode("00100")
//                              .addExtendedDataSegment((byte)10, (byte)10).addReleasedFlag(true).done();
//
//        Map map = builder.getDataMap();
//        byte[] b = builder.pack();
//        ByteArrayInputStream bis = new ByteArrayInputStream(b);
//
//        HelloMsg hm = MsgParser.parseMsg(bis);
//        bis.close();
//        System.out.println("---------------------------------------------------");
//        System.out.println(hm.getTpdu().getValue());
//        System.out.println(hm.getTimestamp());
//        System.out.println(hm.getMti());
//        System.out.println(Arrays.toString(hm.getBitmap().getBitSwitchArray()));
//        System.out.println(hm.getMsgContent().toString());
//        System.out.println(hm.getHeader().toString());
//        System.out.println(hm.getBitmap().toString());
//        System.out.println(hm.getDefinitedField().toString());
//        System.out.println("---------------------------------------------------");
    }
}
