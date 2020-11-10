package com.career.careersidm.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

/**
 * Copyright © 2020 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.io
 * @Author: Winkle.huang.w.k
 * @Date: 2020/11/4
 * @Version: 1.0
 */
public class NettyServer {
	public static void main(String[] args) {
		//这是主要的服务启动器
		ServerBootstrap serverBootstrap = new ServerBootstrap();

		/*======设置线程池=======*/
		//BOSS线程池
		EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
		//WORK
		ThreadFactory threadFactory = new DefaultThreadFactory("work thread pool");
		// CPU个数
		int processorQty = Runtime.getRuntime().availableProcessors();
		EventLoopGroup workLoopGroup = new NioEventLoopGroup(processorQty * 2, threadFactory, SelectorProvider.provider());
		//指定Netty的BOSS线程
		serverBootstrap.group(bossLoopGroup, workLoopGroup);

		/**
		 * 如果是以下这种声明模式, 说明BOSS线程和WORK线程共享一个线程池
		 * (实际上一般情况下, 这种共享线程池的方式已经足够)
		 * serverBootstrao.group(workLoopGroup);
		 */

		/*=========设置服务的通道类型==========*/
		//只能是实现了ServerChannel接口的"服务器"通道类
		serverBootstrap.channel(NioServerSocketChannel.class);
		/**
		 * 也可以这样创建
		 * serverBootstrap.channelFactory(new ChannelFactory<NioServerSocketChannel>() {
		 * 		@override
		 * 		public NioServerSocketChannel new Channel() {
		 * 		 	return new NioServerSocketChannel(SelectorProvider.provider());
		 * 		}
		 * })
		 */

		/*=========设置处理器==========*/
		// 为了演示, 设置了一组简单的ByteArrayDecoder和ByteArrayEncoder
		// Netty的特色就在这一连串通道的处理器
		serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel channel) throws Exception {
				// 字符串编码
				channel.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());
				// 自己写的handler
				channel.pipeline().addLast("handler", new TCPServerHandler());
				// 字符串解码
				channel.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());

			}
		});

		/*=========设置Netty服务器绑定的IP和端口==========*/
		serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 8083));
		// 还可以监控多个端口
		//serverBootstrap.bind(new InetSocketAddress("0.0.0.0", 84));

	}
}
