package com.forcelorda.rpc.remoting.netty.server;

import com.forcelorda.rpc.entity.RpcMessage;
import com.forcelorda.rpc.entity.RpcRequest;
import com.forcelorda.rpc.entity.RpcResponse;
import com.forcelorda.rpc.enums.MessageType;
import com.forcelorda.rpc.enums.RpcResponseCodeEnum;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) {
		log.info("server receive msg: [{}] ", msg);
		MessageType messageType = msg.getHeader().getMessageType();
		
		if (messageType == MessageType.HEARTBEAT) {
			msg.getHeader().setMessageType(MessageType.HEARTBEAT);
			msg.setBody(RpcMessage.PONG);
		} else if(messageType==MessageType.REQUEST) {
			RpcRequest rpcRequest = (RpcRequest) msg.getBody();
			Exception exception = null;
			Object result = null;
			try {
				result = handleRequest(rpcRequest);
				log.info(String.format("server get result: %s", result.toString()));
			} catch (Exception e) {
				exception = e;
			}

			msg.getHeader().setMessageType(MessageType.RESPONSE);
			if (ctx.channel().isActive() && ctx.channel().isWritable()) {
				if (exception==null) {
					RpcResponse rpcResponse = RpcResponse.ok(result);
					msg.setBody(rpcResponse);
				}else {
					RpcResponse rpcResponse = RpcResponse.error(RpcResponseCodeEnum.ERROR,exception);
					msg.setBody(rpcResponse);
				}
				
			} else {
				RpcResponse rpcResponse = RpcResponse.error(RpcResponseCodeEnum.ERROR,exception);
				msg.setBody(rpcResponse);
				log.error("not writable now, message dropped");
			}
		}
		ctx.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

	}

	private Object handleRequest(RpcRequest rpcRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) evt).state();
			if (state == IdleState.READER_IDLE) {
				log.info("idle check happen, so close the connection");
				ctx.close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("server catch exception");
		cause.printStackTrace();
		ctx.close();
	}
}
