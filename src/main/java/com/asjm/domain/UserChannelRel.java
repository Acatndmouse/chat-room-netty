package com.asjm.domain;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 用户id和channel的关联关系处理
 */
public class UserChannelRel {

	private static Map<String, Channel> manager = new ConcurrentHashMap<>();

	public static void put(String senderId, Channel channel) {
		manager.put(senderId, channel);
	}
	
	public static Channel get(String senderId) {
		return manager.get(senderId);
	}

	//判断该用户是否登录
	public static Boolean containsKey(String senderId) {
		return manager.containsKey(senderId);
	}

	public static Map<String, Channel> getManager() {
		return manager;
	}

	public static void output() {
		for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
			System.out.println("UserId: " + entry.getKey() 
							+ ", ChannelId: " + entry.getValue().id().asLongText());
		}
	}
}
