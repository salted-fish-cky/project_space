package com.cky.pojo.vo;

public class FriendRequestVO {

    private String sendUserId;
    private String sendUsername;
    private String SendFaceImage;
    private String sendNickname;


    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendFaceImage() {
        return SendFaceImage;
    }

    public void setSendFaceImage(String sendFaceImage) {
        SendFaceImage = sendFaceImage;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }
}