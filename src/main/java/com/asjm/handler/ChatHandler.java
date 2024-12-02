package com.asjm.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.asjm.domain.ChatMessage;
import com.asjm.domain.MessageType;
import com.asjm.domain.Result;
import com.asjm.domain.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundInvoker;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ChatHandler{
    public static void execute(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame){
        try {
            Channel channel = channelHandlerContext.channel();
            ChatMessage chat = JSON.parseObject(textWebSocketFrame.text(), ChatMessage.class);
            switch (MessageType.match(chat.getType())){
                case PRIVATE:
                    if (StrUtil.isEmpty(chat.getReceiverId())){
                        channel.writeAndFlush(Result.fail("消息发送失败，请指定私聊对象"));
                        return;
                    }
                    Channel targetChannel = UserChannelRel.get(chat.getReceiverId());
                    if (ObjectUtil.isEmpty(targetChannel) || !targetChannel.isActive()){
                        channel.writeAndFlush(Result.fail("消息发送失败，对方未上线"));
                        return;
                    }
                    //走到这里就能正常私聊了
                    channel.writeAndFlush(Result.success(
                                    String.format("私聊消息（%s）:%s",chat.getMsgId(),chat.getMsg())));
                    break;
                case GROUP:
                    channel.writeAndFlush(Result.success(
                            String.format("群聊消息（%s）:%s",chat.getMsgId(),chat.getMsg())));
                    break;
                default:
                    channel.writeAndFlush(Result.fail("不支持的消息类型"));
            }
        }catch (Exception e){

        }
    }
}
