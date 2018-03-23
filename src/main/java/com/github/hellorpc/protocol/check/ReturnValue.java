package com.github.hellorpc.protocol.check;

/**
 *
 * @author dev1
 */
public class ReturnValue {

    private boolean rv = true;
    private String msg;

    public boolean isRv() {
        return rv;
    }

    public ReturnValue setRv(boolean rv) {
        this.rv = rv;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ReturnValue setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
