package com.career.careersidm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: 一个ClientRequestThread线程模拟一个客户端请求。
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/8/13
 * @Version: 1.0
 */
public class ClientRequestThread implements Runnable {

	/**
	 * @author Winkle.Huang.w.k
	 * @description countDownLatch是java提供的同步计数器。
	 *    当计数器数值减为0时，所有受其影响而等待的线程将会被激活。这样保证模拟并发请求的真实性
	 */
	private final CountDownLatch countDownLatch;
	/**
	 * @author Winkle.Huang.w.k
	 * @description 线程的编号
	 */
	private final Integer clientIndex;

	public ClientRequestThread(CountDownLatch countDownLatch, Integer clientIndex) {
		this.countDownLatch = countDownLatch;
		this.clientIndex = clientIndex;
	}

	@Override
	public void run() {
		try (Socket socket = new Socket("localhost", BioSocketServer.DEFAULT_PORT);
			 OutputStream clientRequest = socket.getOutputStream();
			 InputStream clientResponse = socket.getInputStream();
		) {
			// 等待, 直到SocketClientDeamon完成所以线程的启动, 然后所以线程一起发送请求
			this.countDownLatch.await();
			String index = this.clientIndex > 9 ? String.valueOf(this.clientIndex) : "0" + (this.clientIndex );
			//发送请求
			clientRequest.write(("这是第" + index + "个客户端的请求. over").getBytes());
			clientRequest.flush();
			// 在这里等待, 直到服务器返回信息
			System.out.println("第" + index + "个客户端的请求发送完成, 等待服务器返回信息");
			int maxLen = 1024;
			byte[] contextBytes = new byte[maxLen];
			int readLen;
			StringBuilder message = new StringBuilder();
			// 程序执行到这里, 会一直等待服务器返回信息. (注意, 前提是in和out都不能close, 如果close了就收不到服务器的反馈)
			while ((readLen = clientResponse.read(contextBytes, 0, maxLen)) != -1) {
				message.append(new String(contextBytes, 0, readLen));
			}
			message = new StringBuilder(URLDecoder.decode(message.toString(), String.valueOf(StandardCharsets.UTF_8)));
			System.out.println("第" + index + "个客户端接收到来自服务器的信息:" + message);
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
