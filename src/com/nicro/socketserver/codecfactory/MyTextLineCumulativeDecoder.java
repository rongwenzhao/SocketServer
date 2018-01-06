package com.nicro.socketserver.codecfactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * �ۼƵ�decoder �������ݶ�ʧ.
 * 
 * ʹ�ó����� ���緢��<xml>��ʽ���ݣ��������ֱȽϴ�ǰһ�η������һ�η���ƴ����һ�����������
 * 
 * 
 * @author Administrator
 *
 */
public class MyTextLineCumulativeDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
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
				return true;// return true��˵������ /n �ˣ����������ݵĽ�ȡ
			}
		}
		// ��Ҫ�ۼ��ϴε����ݣ������
		in.position(startPosition);
		return false;// false ˵��û���� /n �����������ݽ�ȡ
	}

}
