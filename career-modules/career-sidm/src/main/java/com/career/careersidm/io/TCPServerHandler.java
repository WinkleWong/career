package com.career.careersidm.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/11/4
 * @Version: 1.0
 */
@Slf4j
@Sharable
public class TCPServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 每一个channel, 都有独立的handler, ChannelHandlerContext. ChannelPipeline, Attribute
	 * 所以不需要担心多个channel中的这些对象相互影响
	 * 这里使用content这个key, 记录这个handler中已经接收到的客户端信息.
	 */
	private static final AttributeKey<StringBuffer> CONTENT = AttributeKey.valueOf("content");

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("channelRead(ChannelHandlerContext ctx, Object msg)");
		/**
		 * 使用IDE模拟长连接中的数据缓慢提交
		 * 由read方法负责接受数据, 但只是进行数据累加, 不进行任何处理
		 */
		ByteBuf byteBuf = (ByteBuf) msg;
		try {
			StringBuffer contextBuffer = new StringBuffer();
			while (byteBuf.isReadable()) {
				contextBuffer.append((char) byteBuf.readByte());
			}
			//加入临时区域
			StringBuffer content = ctx.channel().attr(CONTENT).get();
			if (content == null) {
				content = new StringBuffer();
				ctx.channel().attr(CONTENT).set(content);
			}
			content.append(contextBuffer);
		} finally {
			byteBuf.release();
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("channelReadComplete(ChannelHandlerContext ctx)");
		/**
		 * 由readComplate方法负责检查数据是否接收完毕.
		 */
		StringBuffer content = ctx.channel().attr(CONTENT).get();
		// 如果条件成立说明还没有接受到完整客户端信息
		if (content.indexOf("over") == -1) {
			return;
		}

		//当接收到信息后, 首先要做的就是清空原来的历史信息
		ctx.channel().attr(CONTENT).set(new StringBuffer());

		//准备向客户端发送响应
		ByteBuf byteBuf = ctx.alloc().buffer(1024);
		byteBuf.writeBytes("您好客户端:我是服务器,这是回发响应信息！".getBytes());
		ctx.writeAndFlush(byteBuf);

		/**
		 * 关闭, 正常终止这个通道上下文, 就可以关闭通道了
		 * (如果不关闭, 这个通道的会话将一直存在, 只要网络是稳定的, 服务器就可以随时通过这个会话向客户端发送信息)
		 * 关闭通道意味着TCP将正常端开,
		 * 其中所有的handler, ChannelHandlerContext, ChannelPipeline, Attribute等信息都将注销
		 */
		ctx.close();
	}
}
