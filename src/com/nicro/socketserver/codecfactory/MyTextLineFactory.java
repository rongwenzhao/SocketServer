package com.nicro.socketserver.codecfactory;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 自定义的编解码协议工厂
 * 
 * @author Administrator
 *
 */
public class MyTextLineFactory implements ProtocolCodecFactory {

	private final MyTextLineEncoder mEncoder;

	private final MyTextLineDecoder mDecoder;

	private final MyTextLineCumulativeDecoder mCumulativeDecoder;

	public MyTextLineFactory() {
		this.mEncoder = new MyTextLineEncoder();
		this.mDecoder = new MyTextLineDecoder();
		this.mCumulativeDecoder = new MyTextLineCumulativeDecoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return mEncoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return mCumulativeDecoder;
	}

}
