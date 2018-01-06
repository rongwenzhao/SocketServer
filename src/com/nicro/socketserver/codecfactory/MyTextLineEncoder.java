package com.nicro.socketserver.codecfactory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * ��Ҫ���͵���Ϣobjectת��Ϊ�ֽ��룬���������紫��
 * 
 * @author Administrator
 *
 */
public class MyTextLineEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		String value = null;
		if (message instanceof String) {
			value = (String) message;
		}
		if (value != null) {
			CharsetEncoder encoder = (CharsetEncoder) session.getAttribute("encoder");
			if (encoder == null) {
				encoder = Charset.defaultCharset().newEncoder();
				session.setAttribute("encoder", encoder);
			}
			IoBuffer buf = IoBuffer.allocate(value.length());
			buf.setAutoExpand(true);
			buf.putString(value, encoder);
			/**
			 * flip()��Buffer������ģʽ��дģʽ�Ͷ�ģʽ����дģʽ�µ���flip()֮��Buffer��дģʽ��ɶ�ģʽ��
			 * 
			 * ��ôlimit�����ó���position��ǰ��ֵ(����ǰд�˶�������)��postion�ᱻ��Ϊ0���Ա�ʾ�������ӻ����ͷ��ʼ����mark��Ϊ-1��
			 * 
			 * Ҳ����˵����flip()֮�󣬶�/дָ��positionָ��������ͷ�����������������ֻ�ܶ���֮ǰд������ݳ���(���������������������С)
			 */
			buf.flip();

			out.write(buf);
			// ���ˣ�д���������������ݵĲ�������mina������
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
