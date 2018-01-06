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
			// 1������NioSocketAcceptor����
			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			// 2�������Լ�����Ϣ������
			acceptor.setHandler(new MyServerHandler());
			// 3�����ô�����Ϣת������������ʵ����Ϣ�ı���롣 ��Ϣ�����ֽ�������ʽ�������д���ģ���������߻�ȡ���أ���Ҫ���ֽ�����ת��Ϊ�ı����ݡ�
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactory()));
			// ���÷������Ŀ���ʱ�䡣 ����������5*60 ��û�н��ж�����д����������״̬������sessionIdle������
			// IdleStatus.READER_IDLE������������ָ��ʱ����û�ж����ݣ������idle״̬��IdleStatus.WRITER_IDLE������������ָ��ʱ����û��д���ݣ������idle״̬��
			// ��������������������û�������߹��ܡ�
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5 * 60);
			// 4���󶨶˿ڣ���������
			acceptor.bind(new InetSocketAddress(9898));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
