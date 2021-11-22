package com.example.knu_matching.chatting;

import androidx.collection.ArrayMap;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {


    public String roomName;
    public Map<String, Boolean> users = new HashMap<>(); //채팅방 유저

    public String chatRoomUid;
    public Integer roomNum;

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }


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
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String uid;
        public String msg;
        public String nickname;
        public Object timestamp;
    }
}
