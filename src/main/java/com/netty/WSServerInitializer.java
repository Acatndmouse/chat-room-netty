package com.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // websocket基于http协议，所以要有http解码器
        channelPipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        channelPipeline.addLast(new ChunkedWriteHandler());
        // 对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        // 几乎在netty中的编程，都会使用到此handle
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));

        //=====================  以上是用于支持http协议  ===========================================



        // ====================== 增加心跳支持 start    ======================
        // 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
        // 如果是读空闲或者写空闲，不处理
        channelPipeline.addLast(new IdleStateHandler(8, 10, 15));
        // 自定义的空闲状态检测
        channelPipeline.addLast(new HeartBeatHandler());
        // ====================== 增加心跳支持 end    ======================



        // ====================== 以下是支持httpWebsocket ======================
        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由
         * 本handle会帮你处理一些繁重的复杂的事
         * 会帮你处理握手动作：handshaking(close, ping, pong) ping + ping = 心跳
         * 对于websocket来讲，都是以frames进行传输的，不同数据类型对应的frames也不同
         */
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义handle
        channelPipeline.addLast(new ChatHandler());
    }
}
