package com.forcelorda.rpc.remoting.netty.codec;

import java.nio.charset.Charset;
import java.util.List;

import com.forcelorda.rpc.compress.Compress;
import com.forcelorda.rpc.entity.MessageHeader;
import com.forcelorda.rpc.entity.RpcMessage;
import com.forcelorda.rpc.entity.RpcRequest;
import com.forcelorda.rpc.entity.RpcResponse;
import com.forcelorda.rpc.enums.CompressionType;
import com.forcelorda.rpc.enums.MessageType;
import com.forcelorda.rpc.enums.SerializationType;
import com.forcelorda.rpc.factory.CompressFactory;
import com.forcelorda.rpc.factory.SerializerFactory;
import com.forcelorda.rpc.serialization.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 魔数 版本 序列化方式 压缩类型 消息类别 请求id 数据长度 4 1 1 1 1 32 4
 * 请求id为uuid去除-得到的32位字符串f9e1e648ffce48f4bec137a0b124bf5f
 * 
 * @author dova
 *
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < MessageHeader.HEADER_LENGTH) {
			// 可读的数据小于请求头总的大小 直接丢弃
			return;
		}
		// 标记 ByteBuf 读指针位置
		in.markReaderIndex();
		// 魔数
		byte[] magicNumber = new byte[4];
		in.readBytes(magicNumber);
		if (magicNumber != MessageHeader.MAGIC_NUMBER) {
			throw new IllegalArgumentException("magic number is illegal, " + magicNumber);
		}
		// 版本
		byte version = in.readByte();
		// 序列化方式
		byte serializeType = in.readByte();
		// 压缩类型
		byte compressType = in.readByte();
		// 消息类别
		byte msgType = in.readByte();
		// 请求id
		CharSequence requestId = in.readCharSequence(MessageHeader.REQUEST_ID_LENGTH, Charset.forName("UTF-8"));
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			// 可读的数据长度小于 请求体长度 直接丢弃并重置 读指针位置
			in.resetReaderIndex();
			return;
		}
		byte[] body = new byte[dataLength];
		in.readBytes(body);
		// 获取enmu
		SerializationType serializationType = SerializationType.findByCode(serializeType);
		CompressionType compressionType = CompressionType.findByCode(compressType);
		MessageType messageType = MessageType.findByCode(msgType);
		// 组装
		MessageHeader header = new MessageHeader();
		header.setMagicNumber(MessageHeader.MAGIC_NUMBER);
		header.setVersion(version);
		header.setSerializationType(serializationType);
		header.setRequestId(String.valueOf(requestId));
		header.setMessageType(messageType);
		header.setDataLength(dataLength);
		header.setCompressType(compressionType);
		RpcMessage rpcMessage = new RpcMessage();
		rpcMessage.setHeader(header);
		// 解压缩
		Compress compress = CompressFactory.getCompress(header.getCompressType());
		byte[] decompress = compress.decompress(body);
		// 反序列化
		Serializer serializer = SerializerFactory.getSerializer(header.getSerializationType());
		Object object = null;
		if (messageType == MessageType.REQUEST) {
			object = serializer.deserialize(decompress, RpcRequest.class);
		} else if (messageType == MessageType.RESPONSE) {
			object = serializer.deserialize(decompress, RpcResponse.class);
		}else {
			object = serializer.deserialize(decompress, String.class);
		}
		rpcMessage.setBody(object);
		// 输出
		out.add(rpcMessage);

	}

}
