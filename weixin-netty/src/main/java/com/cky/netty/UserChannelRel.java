package com.cky.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户id和channel的关系处理
 */
public class UserChannelRel {


    private static HashMap<String,Channel> manager = new HashMap<>();


    public static void put(String senderId,Channel channel){
        manager.put(senderId,channel);
    }

    public static Channel get(String senderId){
        return manager.get(senderId);
    }

    public static void output(){
        for (HashMap.Entry<String,Channel> entry : manager.entrySet()){
            System.out.println("userId:"+entry.getKey()
                    + "channelId:"+entry.getValue().id().asLongText());
        }
    }

    public static HashMap<String, Channel> getManager() {
        return manager;
    }
}
