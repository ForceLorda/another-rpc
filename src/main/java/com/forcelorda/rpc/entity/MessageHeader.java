package com.forcelorda.rpc.entity;

import com.forcelorda.rpc.enums.CompressionType;
import com.forcelorda.rpc.enums.MessageType;
import com.forcelorda.rpc.enums.SerializationType;

import lombok.Data;
/**
 * 魔数    版本      序列化方式    压缩类型   消息类别  请求id   数据长度
 * 4        1            1           1         1         32         4
 * 请求id为uuid去除-得到的32位字符串f9e1e648ffce48f4bec137a0b124bf5f
 * @author dova
 *
 */
@Data
public class MessageHeader {
	/**
	 * 魔数
	 */
	private byte[] magicNumber;
	/**
	 * 版本号
	 */
	private byte version;
	/**
	 * 序列化方式
	 */
	private SerializationType serializationType;
	
	private CompressionType compressType;
	/**
	 * 请求类型
	 */
	private MessageType messageType;
	
	
	private String requestId;
	/**
	 * 数据长度
	 */
	private int dataLength;
	
	public static final int HEADER_LENGTH = 44;
	public static final int REQUEST_ID_LENGTH = 32;
	
	public static final byte[] MAGIC_NUMBER = new byte[]{'T','R','P','C'};
	
	public static final byte VERSION = 1;
}
