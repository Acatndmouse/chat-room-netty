package com.asjm.handler;

import com.alibaba.fastjson2.JSON;
import com.asjm.domain.Command;
import com.asjm.domain.CommandType;
import com.asjm.domain.Result;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channel
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当客户端连接服务端之后
     * 获取客户端的channel，并放到ChannelGroup中去进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        Command command = JSON.parseObject(textWebSocketFrame.text(), Command.class);
        switch (CommandType.match(command.getCode())){
            case CONNECTION:
                ConnectHandler.execute(channelHandlerContext, command);
                break;
            case CHAT:
                ChatHandler.execute(channelHandlerContext, textWebSocketFrame);
            default:
                channelHandlerContext.channel().writeAndFlush(Result.fail("不支持的code"));

        }
    }
}
