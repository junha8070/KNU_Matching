package com.example.knu_matching.chatting;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {


    public String roomName;
    public Map<String, Boolean> users = new HashMap<>(); //채팅방 유저
    public Map<String,Comment> comments = new HashMap<>(); //채팅 메시지
    public String chatRoomUid;

    public String getChatRoomUid() {
        return chatRoomUid;
    }

    public void setChatRoomUid(String chatRoomUid) {
        this.chatRoomUid = chatRoomUid;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static class Comment
    {
        public String uid;
        public String msg;
        public String nickName;
        public Object timestamp;
    }
}
