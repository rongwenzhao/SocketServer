package com.nicro.socketserver.codecfactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 累计的decoder 不会数据丢失.
 * 
 * 使用场景： 比如发送<xml>格式内容，而内容又比较大。前一次发送与后一次发送拼接在一块才能完整。
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
				return true;// return true，说明遇到 /n 了，进行了数据的截取
			}
		}
		// 需要累计上次的数据，必须的
		in.position(startPosition);
		return false;// false 说明没遇到 /n ，不进行数据截取
	}

}
