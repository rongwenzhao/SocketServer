package com.nicro.socketserver.codecfactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLineDecoder implements ProtocolDecoder {

	/**
	 * ����:��out�л�ȡ��������֮��ת��Ϊ�ַ�����
	 */
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		int startPosition = in.position();
		while (in.hasRemaining()) {
			byte b = in.get();// ÿ�ζ�ȡһ���ֽ�
			// ÿ�ν�ȡһ��
			if (b == '\n') {
				int currentPosition = in.position();// ��ʼ��λ��
				int limit = in.limit();// ��ǰ���ܳ���,��ģʽʱ��limit��ʾ������ܶ�����������
				// ����������ȡ����
				in.position(startPosition);// �ض��򵽿�ʼλ��
				in.limit(currentPosition);// Sets this buffer's limit.������Ҫ��ȡ���ܳ���
				/**
				 * in.slice() ������ԭʼ��������ǰλ�ÿ�ʼ���µĻ�������������ԭʼ��������ʣ����������limit-position����
				 * ��ԭʼ����������һ������Ԫ�������С��ָ�����������м̳�ֻ����ֱ�����ԡ� ����ȡ֮��ķ���ֵ��
				 */
				IoBuffer buf = in.slice();
				byte[] dest = new byte[buf.limit()];
				buf.get(dest);
				String str = new String(dest);
				out.write(str);
				// ���¶�λ�´ζ�ȡ��λ��
				in.position(currentPosition);
				in.limit(limit);
			}
		}
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
