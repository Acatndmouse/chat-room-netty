package com.asjm.domain;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
 
/**
 * @author asjm
 * 消息枚举类：用于明确消息的类型
 **/
@Getter
@AllArgsConstructor
public enum MessageType {
    //私聊
    PRIVATE(1),
    //群聊
    GROUP(2),
    //不支持的类型
    ERROR(-1);
 
    private final Integer type;
 
    public static MessageType match(Integer type) {
        for (MessageType value : MessageType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return ERROR;
    }
}