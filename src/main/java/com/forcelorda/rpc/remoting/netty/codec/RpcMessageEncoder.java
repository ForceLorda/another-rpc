package com.forcelorda.rpc.remoting.netty.codec;

import java.nio.charset.Charset;

import com.forcelorda.rpc.compress.Compress;
import com.forcelorda.rpc.entity.MessageHeader;
import com.forcelorda.rpc.entity.RpcMessage;
import com.forcelorda.rpc.factory.CompressFactory;
import com.forcelorda.rpc.factory.SerializerFactory;
import com.forcelorda.rpc.serialization.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 魔数    版本      序列化方式    压缩类型   消息类别  请求id   数据长度
 * 4        1            1           1         1         32         4
 * 请求id为uuid去除-得到的32位字符串f9e1e648ffce48f4bec137a0b124bf5f
 * @author dova
 *
 */
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
		
		MessageHeader header = msg.getHeader();
		// 魔数
		out.writeBytes(header.getMagicNumber());
		// 版本
		out.writeByte(header.getVersion());
		// 序列化方式
		out.writeByte(header.getSerializationType().getCode());
		// 压缩类型
		out.writeByte(header.getCompressType().getCode());
		// 消息类别
		out.writeByte(header.getMessageType().getCode());
		// 请求id
		out.writeCharSequence(header.getRequestId(), Charset.forName("UTF-8"));
		// 数据长度
		out.writeInt(msg.getHeader().getDataLength());
		Serializer serializer = SerializerFactory.getSerializer(header.getSerializationType());
		byte[] 	serializerData = serializer.serialize(msg.getBody());
		Compress compress = CompressFactory.getCompress(header.getCompressType());
		byte[] compressData = compress.compress(serializerData);
		// 数据
		out.writeBytes(compressData);
	}

}
