/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.github.hellorpc.protocol.entities;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class ProtocolVersion {

    /**
     * 是否是生产版本，如果是false，则表示是开发版本
     */
    private boolean released = false;
    private short version;

    public ProtocolVersion() {
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ProtocolVersion{" + "released=" + released + ", version=" + version + '}';
    }
}
