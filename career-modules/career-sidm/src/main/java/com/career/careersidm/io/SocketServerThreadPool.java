package com.career.careersidm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/9/1
 * @Version: 1.0
 */
public class SocketServerThreadPool implements Runnable {

	private Socket socket;

	public SocketServerThreadPool(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (InputStream in = socket.getInputStream();
			 OutputStream out = socket.getOutputStream();) {
			int sourcePort = socket.getPort();
			final int maxLen = 1024;
			byte[] contextBytes = new byte[maxLen];
			int readLen = in.read(contextBytes, 0, maxLen);
			String message = new String(contextBytes, 0, readLen);
			System.out.println("服务器收到来自于端口：[" + sourcePort + "] 的信息：{" + message + "}");
			out.write("回发响应信息!".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
