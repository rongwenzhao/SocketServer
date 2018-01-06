package com.nicro.socketserver.codecfactory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 将要发送的信息object转化为字节码，来进行网络传输
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
			 * flip()：Buffer有两种模式，写模式和读模式。在写模式下调用flip()之后，Buffer从写模式变成读模式。
			 * 
			 * 那么limit就设置成了position当前的值(即当前写了多少数据)，postion会被置为0，以表示读操作从缓存的头开始读，mark置为-1。
			 * 
			 * 也就是说调用flip()之后，读/写指针position指到缓冲区头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)
			 */
			buf.flip();

			out.write(buf);
			// 至此，写出二进制网络数据的操作就由mina来处理。
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
