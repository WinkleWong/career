package com.career.careersidm.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/10/28
 * @Version: 1.0
 * <p>
 * 负责对每一个socketChannel的数据获取事件进行监听
 * 重要的说明: 一个socketChannel都会有一个独立工作的SocketChannelReadHandler对象,
 * 其中又都将共享一个"文件状态标示"对象FileDescriptor
 * 一个独立的自己定义的Buffer缓存(这里使用的是ByteBuffer)
 * 所以不用担心在服务器端回出现"窜对象"这种情况, 因为JAVA AIO框架已经帮你组织好了
 * 但是最重要的是, 用于生成Channel的对象: AsynchronousChannelProvider是单例模式, 无论在哪组socketChannel都是一个对象引用
 */
@Slf4j
public class SocketChannelReadHandler implements CompletionHandler<Integer, StringBuffer> {

	private final AsynchronousSocketChannel socketChannel;

	/**
	 * 专门用于进行通道数据缓存操作的ByteBuffer
	 */
	private final ByteBuffer byteBuffer;

	public SocketChannelReadHandler(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
		this.socketChannel = socketChannel;
		this.byteBuffer = byteBuffer;
	}

	@SneakyThrows
	@Override
	public void completed(Integer result, StringBuffer historyContext) {
		if (result == -1) {
			// 如果条件成立, 说明客户端主动终止了TCP套接字, 这时服务端终止就可以了
			try {
				this.socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		log.info("completed(Integer result, Void attachment): 取出通道中准备好的值");
		/**
		 * 实际上, 从Integer result知道了本次channel从操作系统获取数据总长度
		 * 所以并不需要切换成"读模式", 但是为了编码的规范性, 还是建议进行切换
		 * 另外, 无论是JAVA AIO还是NIO, 都会出现buffer的总容量小于当前从操作系统获取到的总数据量,
		 * 但区别是, JAVA NIO中不需要专门处理这样
		 */

		this.byteBuffer.flip();
		byte[] context = new byte[1024];
		this.byteBuffer.get(context, 0, result);
		this.byteBuffer.clear();
		String newContent = new String(context, 0, result, StandardCharsets.UTF_8);
		historyContext.append(newContent);
		log.info("================目前的传输结果：" + historyContext);
		if (historyContext.indexOf("over") == -1) {
			//如果条件成立说明还没有接收到"结束标记"
			return;
		} else {
			this.byteBuffer.clear();
			log.info("客户端发来的信息======message :" + historyContext);
			/**
			 * 接受完成后, 可以在这里正式处理业务了
			 */

			//回发数据并关闭channel
			ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("你好客户端,这是服务器的返回数据", StandardCharsets.UTF_8.name()).getBytes());
			socketChannel.write(sendBuffer);
			socketChannel.close();
		}

		log.info("=======收到完整信息，开始处理业务=========");
		historyContext = new StringBuffer();
		//还要继续监听(一次监听一次通知)
		this.socketChannel.read(this.byteBuffer, historyContext, this);
	}

	@Override
	public void failed(Throwable exc, StringBuffer attachment) {
		log.error("=====发现客户端异常关闭，服务器将关闭TCP通道");
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
