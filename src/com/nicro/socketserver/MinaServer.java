package com.nicro.socketserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.nicro.socketserver.codecfactory.MyTextLineFactory;

public class MinaServer {

	public static void main(String[] args) {
		try {
			// 1、创建NioSocketAcceptor对象
			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			// 2、设置自己的消息处理器
			acceptor.setHandler(new MyServerHandler());
			// 3、配置处理消息转换的拦截器来实现消息的编解码。 消息是以字节流的形式在网络中传输的，服务器这边获取后呢，需要将字节数据转换为文本数据。
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactory()));
			// 设置服务器的空闲时间。 当服务器在5*60 秒没有进行读或者写，则进入空闲状态，调用sessionIdle方法。
			// IdleStatus.READER_IDLE，当服务器在指定时间内没有读数据，则进入idle状态；IdleStatus.WRITER_IDLE，当服务器在指定时间内没有写数据，则进入idle状态。
			// 用来进行心跳管理，长久没连踢下线功能。
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5 * 60);
			// 4、绑定端口，开启服务
			acceptor.bind(new InetSocketAddress(9898));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
