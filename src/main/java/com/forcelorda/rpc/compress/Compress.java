package com.forcelorda.rpc.compress;

public interface Compress {

	byte[] compress(byte[] bytes);

	byte[] decompress(byte[] bytes);

}
