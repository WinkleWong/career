package com.career.careersidm.io;

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
public class BioSocketClient {
	public static void main(String[] args) throws Exception {
		int clientNumber = 20;
		CountDownLatch countDownLatch = new CountDownLatch(clientNumber);

		// 分别开始启动这20个客户端,并发访问
		for (int index = 0; index < clientNumber; index++, countDownLatch.countDown()) {
			ClientRequestThread client = new ClientRequestThread(countDownLatch, index);
			new Thread(client).start();
		}

		// 这个wait不涉及到具体的实验逻辑，只是为了保证守护线程在启动所有线程后，进入等待状态
		synchronized (BioSocketClient.class) {
			BioSocketClient.class.wait();
		}
	}
}
