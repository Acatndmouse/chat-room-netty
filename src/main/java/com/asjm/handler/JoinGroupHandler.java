package com.asjm.handler;

import com.asjm.domain.Result;
import com.asjm.domain.UserChannelRel;
import io.netty.channel.ChannelHandlerContext;

public class JoinGroupHandler {
    public static void execute(ChannelHandlerContext channelHandlerContext) {
        //如果不存在就加入
//        if (!WebSocketHandler.users.contains(channelHandlerContext.channel())){
//            WebSocketHandler.users.add(channelHandlerContext.channel());
//        }
        //客户端连接服务端时已经默认加入系统群聊了
        channelHandlerContext.channel().writeAndFlush(Result.success("加入系统默认群聊成功"));
    }
}
