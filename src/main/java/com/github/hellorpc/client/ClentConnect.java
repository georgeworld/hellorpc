package com.github.hellorpc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ClentConnect {
	
	public static HttpURLConnection conn = null ;
	
	public ClentConnect(String host, int port , String uri) {
		if (conn ==null) {
			URL url;
			try {
				System.out.println("ClentConnect.connect");
				url = new URL("http://"+host+":"+port+"/"+uri);
				conn = (HttpURLConnection) url.openConnection();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public HttpURLConnection setConn() {
		try {
			conn.setRequestMethod("POST");
			// 提交模式
			// conn.setConnectTimeout(10000);//连接超时 单位毫秒
			// conn.setReadTimeout(2000);//读取超时 单位毫秒
			conn.setDoOutput(true);// 是否输入参数
			// 表单参数与get形式一样
			conn.addRequestProperty("Accept","text/html, application/xhtml+xml, */*");  
			conn.addRequestProperty("Accept-Language","zh-CN,en-US;q=0.5");  
			conn.addRequestProperty("User-Agent","Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");  
			conn.addRequestProperty("Accept-Encoding","gzip, deflate");  
			conn.addRequestProperty("Connection","Keep-Alive");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn ;
	}
	
	public InputStream loadClass() {
		
		try {
			StringBuffer params = new StringBuffer();
			this.setConn() ;
			
			OutputStream clientoutput = conn.getOutputStream() ;
			params.append("&method=loadclass");
			clientoutput.write(params.toString().getBytes());// 输入参数
			
			InputStream inStream=conn.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(inStream));
			
				
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
	
	
	public void callServer(Object[] obj , String method) {
		
			this.setConn() ;
        
	        OutputStream clientoutput;
			try {
				clientoutput = conn.getOutputStream();
				clientoutput.write("HTTP/1.1 200 OK\n".getBytes());
				clientoutput.write("Content-Type: text/html;charset=utf8\n".getBytes());
				clientoutput.write("\n".getBytes());
				
				
				InputStream inStream=conn.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	

}
