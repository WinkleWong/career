package com.career.careersidm.io;

import com.google.common.base.Utf8;
import org.apache.tomcat.util.digester.DocumentProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/8/13
 * @Version: 1.0
 */
public class ClientRequestThread implements Runnable {

	private final CountDownLatch countDownLatch;
	private final Integer clientIndex;

	public ClientRequestThread(CountDownLatch countDownLatch, Integer clientIndex) {
		this.countDownLatch = countDownLatch;
		this.clientIndex = clientIndex;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket("localhost", 8083);
			 OutputStream clientRequest = socket.getOutputStream();
			 InputStream clientResponse = socket.getInputStream();
		) {
			// 等待, 直到SocketClientDeamon完成所以线程的启动, 然后所以线程一起发送请求
			this.countDownLatch.await();

			//发送请求
			clientRequest.write(("这是第" + this.clientIndex + " 个客户端的请求. over").getBytes());
			clientRequest.flush();

			// 在这里等待, 直到服务器返回信息
			System.out.println("第" + this.clientIndex + "个客户端的请求发送完成, 等待服务器返回信息");
			int maxLen = 1024;
			byte[] contextBytes = new byte[maxLen];
			int readLen;
			String message = "";
			// 程序执行到这里, 会一直等待服务器返回信息. (注意, 前提是in和out都不能close, 如果close了就收不到服务器的反馈)
			while ((readLen = clientResponse.read(contextBytes, 0, maxLen)) != -1) {
				message += new String(contextBytes, 0, readLen);
			}
			message = URLDecoder.decode(message, String.valueOf(StandardCharsets.UTF_8));
			System.out.println("第" + this.clientIndex + "个客户端接收到来自服务器的信息:" + message);
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
