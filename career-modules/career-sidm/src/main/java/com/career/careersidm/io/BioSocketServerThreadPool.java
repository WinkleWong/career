package com.career.careersidm.io;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/9/1
 * @Version: 1.0
 */
public class BioSocketServerThreadPool {
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
			5, 60,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1024),
			new ThreadFactoryBuilder().setNameFormat("FeignNIO").build(),
			new ThreadPoolExecutor.AbortPolicy());

	public static void main(String[] args) {
		final int defaultPort = 8083;
		try (ServerSocket serverSocket = new ServerSocket(defaultPort)) {
			System.out.println("监听来自于" + defaultPort + "的端口信息");
			while (true) {
				Socket socket = serverSocket.accept();
				SocketServerThreadPool socketServerThreadPool = new SocketServerThreadPool(socket);
				THREAD_POOL.execute(socketServerThreadPool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
