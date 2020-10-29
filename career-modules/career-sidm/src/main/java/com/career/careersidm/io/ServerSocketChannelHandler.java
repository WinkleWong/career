package com.career.careersidm.io;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: 这个处理类, 专门用来响应ServerSocketChannel的事件
 * ServerSocketChannel只有一种事件: 接受客户端的连接
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/10/28
 * @Version: 1.0
 */
@Slf4j
public class ServerSocketChannelHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
	private final AsynchronousServerSocketChannel serverSocketChannel;

	public ServerSocketChannelHandler(AsynchronousServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}

	@Override
	public void completed(AsynchronousSocketChannel result, Void attachment) {
		log.info("completed(AsynchronousSocketChannel result, Void attachment)");
		//每次都要重新注册监听(一次注册一次响应), 但是由于"文件状态标识符"是独享的, 所以不需要担心有漏掉的事件
		this.serverSocketChannel.accept(attachment, this);
		//为这个新的socketChannel注册READ事件, 以便操作系统在收到数据并准备好后, 主动通知应用程序.
		// 在这里, 由于要将这个客户端多次传数数据累加起来一起处理, 所以将一个stringbuffer对象作为一个附件依附在这个channel上
		ByteBuffer readBuffer = ByteBuffer.allocate(2550);
		result.read(readBuffer, new StringBuffer(), new SocketChannelReadHandler(result, readBuffer));
	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		log.error("failed(Throwable exc, Void attachment)");
	}
}
