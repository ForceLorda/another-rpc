package com.forcelorda.rpc.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.forcelorda.rpc.compress.Compress;
import com.forcelorda.rpc.compress.gzip.GzipCompress;
import com.forcelorda.rpc.enums.CompressionType;

public class CompressFactory {
	
	private static ConcurrentHashMap<CompressionType, Compress> map = new ConcurrentHashMap<>();
	
	static {
		map.put(CompressionType.GZIP, new GzipCompress());
	}
	
	public static Compress getCompress(CompressionType comparisonType) {
		Compress compress = map.get(comparisonType);
		if (compress==null) {
			compress = map.get(CompressionType.GZIP);
		}
		return compress;
	}
}
