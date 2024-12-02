package com.asjm.domain;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author asjm
 **/
@Data
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 3611169682695799175L;

    private Integer type;           // 消息类型
    private String senderId;		// 发送者的用户id
    private String receiverId;		// 接受者的用户id
    private String msg;				// 聊天内容
    private String msgId;			// 用于消息的签收
}