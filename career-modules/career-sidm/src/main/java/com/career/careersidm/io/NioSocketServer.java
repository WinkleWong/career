package com.career.careersidm.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/10/14
 * @Version: 1.0
 */
public class NioSocketServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		ServerSocket serverSocket = serverChannel.socket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(8083));

		Selector selector = Selector.open();
		/*注意: 服务器通道只能注册SelectionKey.ON_ACCEPT事件*/
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		try {
			while (true) {
				// 如果条件成立. 说明本次询问selector, 并没有获取到任何准备好的/感兴趣的事件
				// Java程序对多路复用IO的支持也包括阻塞模式和非阻塞模式两种
				if (selector.select(100) == 0) {
					continue;
				}
				// 这里就是本次询问操作系统, 所获取到的"所关心的事件"的事件类型(每一个通道都是独立的)
				Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

				while (selectionKeys.hasNext()) {
					SelectionKey readyKey = selectionKeys.next();
					// 这个已经处理的readyKey一定要一处. 如果不移除, 就会一直存在selector.selectedKeys集合中
					// 待到下一次selector.select() > 0 时, 这个readyKey又会被处理一次
					selectionKeys.remove();

					SelectableChannel selectableChannel = readyKey.channel();
					if (readyKey.isValid() && readyKey.isAcceptable()) {
						System.out.println("======channel通道已经准备好=======");
						/**
						 * 当server socket channel通道已经准备好, 就可以从server socket channel中获取socketChannel了
						 * 拿到socket channel 后, 要做的事情就是马上到selector注册这个socket channel感兴趣的事情
						 * 否则无法监听到这个socket channel到达的数据
						 */
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking(false);
						/*socket 通道可以且只可以注册三种事件 SelectionKey.OP_READ, SelectionKey.OP_WRITE, SelectionKey.OP_CONNECT*/
						socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
					} else if (readyKey.isValid() && readyKey.isConnectable()) {
						System.out.println("======socket channel 建立连接=======");
					} else if (readyKey.isValid() && readyKey.isReadable()) {
						System.out.println("======socket channel 数据准备完成，可以读取=======");
						readSocketChannel(readyKey);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}

		//这个wait不涉及到具体的实验逻辑，只是为了保证守护线程在启动所有线程后，进入等待状态
		synchronized (NioSocketServer.class) {
			NioSocketServer.class.wait();
		}
	}

	/**
	 *
	 * @param readyKey
	 * @throws Exception
	 */
	private static void readSocketChannel(SelectionKey readyKey) throws Exception {
		SocketChannel clientSocketChannel = (SocketChannel) readyKey.channel();
		// 获取客户端使用的端口
		InetSocketAddress sourceSocketAddress = (InetSocketAddress) clientSocketChannel.getRemoteAddress();
		int resourcePort = sourceSocketAddress.getPort();

		// 拿到这个socket channel使用的缓存区, 准备读取数据
		ByteBuffer contextBytes = (ByteBuffer) readyKey.attachment();
		// 将通道的数据写入到缓存区, 注意是写入到缓存区
		// 由于之前设置了ByteBuffer的大小为2048byte, 所以可以存在写入不完的情况
		int readLen;
		try {
			readLen = clientSocketChannel.read(contextBytes);
		} catch (Exception e) {
			// 这里抛出异常, 一般就是客户端因为某种原因zhong'zhi终止了, 所以关闭channel就行了.
			e.printStackTrace();
			clientSocketChannel.close();
			return;
		}

		// 如果缓存区中没有任何数据(但实际上这个不太可能, 否则就不会触发OP_READ事件了)
		if (readLen == -1) {
			System.out.println("====缓存区没有数据====");
			return;
		}

		// 将缓存区从写状态切换为读状态(这个方法是读写模式互切换)
		// 这是Java NIO框架中的这个socket channel的写请求将全部等待
		contextBytes.flip();

		// 注意中文乱码问题, Java NIO 本身也提供编解码方式
		byte[] messageBytes = contextBytes.array();
		String messageEncode = new String(messageBytes, StandardCharsets.UTF_8);
		String message = URLDecoder.decode(messageEncode, StandardCharsets.UTF_8.name());

		// 如果收到"over" 关键字, 才会清空buffer, 并回发数据
		// 否则不清空缓存, 还要还原buffer的 "写状态"
		final String overFlag = "over";
		if (message.contains(overFlag)) {
			// 清空已读取的缓存, 并从新切换为写状态(这里要注意clear() 和capacity()两个方法的区别)
			contextBytes.clear();
			System.out.println("端口:" + resourcePort + "客户端发来的信息======message : [" + message + "]");
			// 当接收完成后，可以在这里正式处理业务了

			/**
			 * business code
			 */

			// 回发数据, 并关闭channel
			ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("你好客户端,这是服务器的返回数据", StandardCharsets.UTF_8.name()).getBytes());
			clientSocketChannel.write(sendBuffer);
			clientSocketChannel.close();
		} else {
			System.out.println("端口:" + resourcePort + "客户端信息还未接受完，继续接受======message : [" + message + "]");
			// 这是, limit和capacity的值一样, position的位置是readLen的位置
			contextBytes.position(readLen);
			contextBytes.limit(contextBytes.capacity());
		}

	}

}
