package com.career.careersidm.io;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/10/22
 * @Version: 1.0
 */
public class AioSocketServer {
	private static final Object waitObject = new Object();


	public static void main(String[] args) throws IOException, InterruptedException {
		/**
		 * 对于使用线程池技术
		 * 1. Executors是线程池生成工具, 通过这个工具可以很轻松地生成 "固定大小地线程池", "调度池", "可伸缩线程数量的池"
		 * 2. 也可以通过ThreadPoolExecutor直接生成池
		 * 3. 这个线程池是用来得到操作系统的IO事件通知的, 不是用来进行得到IO数据后的业务处理的, 要进行后者的操作, 可以再使用一个池, 最好不要混用
		 * 4. 也可以不使用线程池, 如果决定不使用线程池, 直接AsynchronousServerSocketChannel.open()就可以了
		 */

		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("aio-pool-%d").build();
		ExecutorService threadPool = new ThreadPoolExecutor(5, 200,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(threadPool);
		final AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open(group);

		//设置要监听的端口"0.0.0.0"代表本机所有IP设备
		serverSocket.bind(new InetSocketAddress("0.0.0.0", 8083));
		//为AsynchronousServerSocketChannel注册监听, 注意只是为AsynchronousServerSocketChannel通道注册监听, 并不包括为随后客户端和服务器socketChannel通道注册的监听
		serverSocket.accept(null, new ServerSocketChannelHandler(serverSocket));

		//等待. 以便观察现象(这个与AIO无关, 只是为了保证守护线程不会退出)
		synchronized (waitObject) {
			waitObject.wait();
		}
	}
}
