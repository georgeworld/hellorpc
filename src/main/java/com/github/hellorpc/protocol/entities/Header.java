package com.github.hellorpc.protocol.entities;

import java.util.Arrays;

/**
 * 报文头段
 *
 * @author George <Georgeinfo@163.com>
 */
public class Header {

    /**
     * Header Field 1：报文头长度
     */
    private int headerLength;
    /**
     * Header Field 2：报文头标识和报文版本号
     */
    private ProtocolVersion protocolVersion = new ProtocolVersion();
    /**
     * Header Field :3：整个报文的总长度
     */
    private long wholeMsgLength;
    /**
     * Header Field 4：报文数据规则
     */
    private DataRule dataRule = new DataRule();
    /**
     * Header Field 5：保留使用报文头数据段，2个字节的长度
     */
    private byte[] extendedDataSegment;
    /**
     * Header Field 6：响应码，5个字节的定长字符串
     */
    private String actionCode;

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public long getWholeMsgLength() {
        return wholeMsgLength;
    }

    public void setWholeMsgLength(long wholeMsgLength) {
        this.wholeMsgLength = wholeMsgLength;
    }

    public DataRule getDataRule() {
        return dataRule;
    }

    public void setDataRule(DataRule dataRule) {
        this.dataRule = dataRule;
    }

    public byte[] getExtendedDataSegment() {
        return extendedDataSegment;
    }

    public void setExtendedDataSegment(byte[] extendedDataSegment) {
        this.extendedDataSegment = extendedDataSegment;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    @Override
    public String toString() {
        return "Header{" + "headerLength=" + headerLength + ", protocolVersion=" + protocolVersion + ", wholeMsgLength=" + wholeMsgLength + ", dataRule=" + dataRule + ", extendedDataSegment=" + Arrays.toString(extendedDataSegment) + ", actionCode=" + actionCode + '}';
    }
}
