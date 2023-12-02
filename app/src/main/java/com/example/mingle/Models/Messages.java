package com.example.mingle.Models;

public class Messages {

      String mid;
      String msg;

      String msgid;
      Long timestamp;

    public Messages(String mid, String msg, Long timestamp) {
        this.mid = mid;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public Messages(String mid, String msg) {
        this.mid = mid;
        this.msg = msg;
    }
    public Messages()
    {

    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
