<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="发送消息" prop="msgContent">
        <el-input v-model="form.msgContent" placeholder="请输入信息"/>
        <el-button type="primary" @click="send()">点我发送</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Message",
  data() {
    return {
      socket: null,
      receiveMsg: null,
      // 表单参数
      form: {
        msgContent: null
      },
      MsgActionEnum: {
        CONNECT: 1, 	// 第一次(或重连)初始化连接
        CHAT: 2, 		// 聊天消息
        KEEPALIVE: 3, 	// 客户端保持心跳
      }
    };
  },
  created() {
    this.initChat();
  },
  methods: {
    initChat() {
      if (window.WebSocket) {
        // 如果当前的状态已经连接，那就不需要重复初始化websocket
        if (this.socket != null
          && this.socket != undefined
          && this.socket.readyState == WebSocket.OPEN) {
          return false;
        }

        this.socket = new WebSocket("ws://127.0.0.1:8081/ws");
        this.socket.onopen = this.wsOpen;
        this.socket.onclose = this.wsClose;
        this.socket.onerror = this.wsError;
        this.socket.onmessage = this.wsMessage;
      } else {
        alert("浏览器不支持websocket协议...");
      }
    },
    wsOpen() {
      console.log("websocket连接已建立...");

      let chatMsg = new this.chatMsg('dabu', null, null, null);
      // 构建DataContent
      let dataContent = new this.dataContent(this.MsgActionEnum.CONNECT, chatMsg, null);

      // 发送websocket
      this.chat(JSON.stringify(dataContent));

      // 定时发送心跳
      setInterval(this.keepalive, 12000);
    },

    wsMessage(e) {
      console.log("接受到消息：" + e.data);

      // 转换DataContent为对象
      // let dataContent = JSON.parse(e.data);
      // let action = dataContent.action;
      //
      // let chatMsg = dataContent.chatMsg;
      // let msg = chatMsg.msg;
      // let friendUserId = chatMsg.senderId;
      // let myId = chatMsg.receiverId;

      // var dataContentSign = new this.dataContent(this.MsgActionEnum.SIGNED, null, chatMsg.msgId);
      // CHAT.chat(JSON.stringify(dataContentSign));
    },
    wsClose() {
      console.log("连接关闭...");
    },
    wsError() {
      console.log("发生错误...");
    },
    /** 提交按钮 */
    chat(msg) {
      // 如果当前websocket的状态是已打开，则直接发送， 否则重连
      if (this.socket != null && this.socket != undefined && this.socket.readyState == WebSocket.OPEN) {
        this.socket.send(msg);
      } else {
        // 重连websocket
        this.init();
        setTimeout("this.reChat(this.form.msgContent)", 1000);
      }
    },
    reChat(msg) {
      console.log("消息重新发送...");
      this.socket.send(msg);
    },

    keepalive() {
      console.info("ping")
      // 构建DataContent
      let dataContent = new this.dataContent(this.MsgActionEnum.KEEPALIVE, null, null);
      // 发送心跳
      this.chat(JSON.stringify(dataContent));
    },
    /**
     * 和后端的 ChatMsg 聊天模型对象保持一致
     * @param {Object} senderId
     * @param {Object} receiverId
     * @param {Object} msg
     * @param {Object} msgId
     */
    chatMsg(senderId, receiverId, msg, msgId){
      this.senderId = senderId;
      this.receiverId = receiverId;
      this.msg = msg;
      this.msgId = msgId;
    },

    /**
     * 构建消息 DataContent 模型对象
     * @param {Object} action
     * @param {Object} chatMsg
     * @param {Object} extend
     */
    dataContent(action, chatMsg, extend){
      this.action = action;
      this.chatMsg = chatMsg;
      this.extend = extend;
    },
    send(){
      // 构建ChatMsg
      let chatMsg = new this.chatMsg('dabu', 'dabu', this.form.msgContent, null);
      // 构建DataContent
      let dataContent = new this.dataContent(this.MsgActionEnum.CHAT, chatMsg, null);
      this.chat(JSON.stringify(dataContent))
    }
  }
};
</script>
