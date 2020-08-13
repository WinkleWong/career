package com.career.careersidm.io;

import java.net.ServerSocket;
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
public class BioSocketServer {
	private static int DEFAULT_PORT = 8083;

	public static void main(String[] args) throws InterruptedException {
		try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
			System.out.println("监听来自于" + DEFAULT_PORT + "的端口信息");
			while (true) {
				Socket socket = serverSocket.accept();
				SocketServerThread socketServerThread = new SocketServerThread(socket);
				new Thread(socketServerThread).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		//这个wait不涉及到具体的实验逻辑，只是为了保证守护线程在启动所有线程后，进入等待状态
		synchronized (BioSocketServer.class) {
			BioSocketServer.class.wait();
		}
	}
}
