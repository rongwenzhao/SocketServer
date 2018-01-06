package com.nicro.socketserver.codecfactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLineDecoder implements ProtocolDecoder {

	/**
	 * 解码:从out中获取二进制码之后转换为字符串。
	 */
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		int startPosition = in.position();
		while (in.hasRemaining()) {
			byte b = in.get();// 每次读取一个字节
			// 每次截取一行
			if (b == '\n') {
				int currentPosition = in.position();// 开始的位置
				int limit = in.limit();// 当前的总长度,读模式时，limit表示你最多能读到多少数据
				// 接下来做截取操作
				in.position(startPosition);// 重定向到开始位置
				in.limit(currentPosition);// Sets this buffer's limit.即设置要截取的总长度
				/**
				 * in.slice() 创建从原始缓冲区当前位置开始的新的缓冲区，容量是原始缓冲区的剩余容量，（limit-position），
				 * 与原始缓冲区共享一段数据元素子序列。分割出来的子序列继承只读和直接属性。 即截取之后的返回值。
				 */
				IoBuffer buf = in.slice();
				byte[] dest = new byte[buf.limit()];
				buf.get(dest);
				String str = new String(dest);
				out.write(str);
				// 重新定位下次读取的位置
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
