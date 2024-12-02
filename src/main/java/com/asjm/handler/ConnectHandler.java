package com.asjm.handler;

import com.alibaba.fastjson2.JSON;
import com.asjm.IMServer;
import com.asjm.domain.Command;
import com.asjm.domain.Result;
import com.asjm.domain.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ConnectHandler {

    public static void execute(ChannelHandlerContext channelHandlerContext , Command command){
        Channel channel = channelHandlerContext.channel();
        if (UserChannelRel.containsKey(command.getNickname())){
            channel.writeAndFlush(Result.fail("该用户已上线，请换个昵称试试"));
            //断开连接
            channel.disconnect();
            return;
        }

        //把用户id和Channel关联起来
        UserChannelRel.put(command.getNickname(), channel);

        channel.writeAndFlush(Result.success("与服务端建立连接成功"));
        //返回群聊的人
        channel.writeAndFlush(Result.success(JSON.toJSONString(UserChannelRel.getManager().keySet())));
    }
}
