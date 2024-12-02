package com.asjm.domain;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
 
/**
 * @author asjm
 * 聊天的命令枚举,用于判断发送的数据类型
 **/
@Getter
@AllArgsConstructor
public enum CommandType {
    CONNECTION(1001),
    CHAT(1002),
    JOIN_GROUP(1003),
    ERROR(-1);
 
    private final Integer code;
 
    public static CommandType match(Integer code) {
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ERROR;
    }
}