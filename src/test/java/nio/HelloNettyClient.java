package nio;

import com.github.hellorpc.nio.client.AbstractHelloRPCNetty;

import java.net.InetSocketAddress;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public class HelloNettyClient extends AbstractHelloRPCNetty {
    private InetSocketAddress address;

    public HelloNettyClient() {
        this.address = new InetSocketAddress("127.0.0.1", 9919);
    }

    @Override
    public void setAddress(InetSocketAddress serverAddress) {
        this.address = serverAddress;
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }
}
