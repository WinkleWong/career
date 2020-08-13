package com.career.careersidm.io;

import lombok.SneakyThrows;

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
 * @Date: 2020/8/13
 * @Version: 1.0
 */
public class SocketServerThread implements Runnable {
	private final Socket socket;

	public SocketServerThread(Socket socket) {
		this.socket = socket;
	}

	@SneakyThrows
	@Override
	public void run() {
		try (InputStream in = socket.getInputStream();
			 OutputStream out = socket.getOutputStream()) {
			int sourcePort = socket.getPort();
			int maxLen = 1024;
			byte[] contextBytes = new byte[maxLen];
			// 使用线程, 同样无法解决read方法的阻塞问题,
			// 也就是说, read方法处同样会被阻塞, 直到操作系统有数据准备好
			int readLen = in.read(contextBytes, 0, maxLen);
			String message = new String(contextBytes, 0, readLen);
			System.out.println("服务器收到来自于端口：" + sourcePort + "的信息：" + message);
			out.write("回发响应信息！".getBytes());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (this.socket != null) {
					this.socket.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
