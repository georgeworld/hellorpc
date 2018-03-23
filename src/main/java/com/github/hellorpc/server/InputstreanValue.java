package com.github.hellorpc.server;

import java.io.InputStream;

public class InputstreanValue {
	
	public InputStream serverInputstream ;
	public boolean flag = false ;
	public InputStream getServerInputstream() {
		return serverInputstream;
	}
	public void setServerInputstream(InputStream serverInputstream) {
		this.serverInputstream = serverInputstream;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
