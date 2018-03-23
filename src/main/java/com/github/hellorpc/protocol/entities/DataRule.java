package com.github.hellorpc.protocol.entities;

/**
 * 数据规则定义
 *
 * @author George <Georgeinfo@163.com>
 */
public class DataRule {

    /**
     * 是否对报文内容数据进行压缩
     */
    private boolean compression = false;
    /**
     * 是否对报文内容进行加密
     */
    private boolean encryption = false;
    /**
     * 是否长连接
     */
    private boolean connkeepalive = false;

    public DataRule() {
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public boolean isConnkeepalive() {
        return connkeepalive;
    }

    public void setConnkeepalive(boolean connkeepalive) {
        this.connkeepalive = connkeepalive;
    }

    @Override
    public String toString() {
        return "DataRule{" + "compression=" + compression + ", encryption=" + encryption + ", connkeepalive=" + connkeepalive + '}';
    }

}
