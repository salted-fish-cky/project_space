package com.cky.netty;

import com.cky.SpringUtil;
import com.cky.enums.MsgActionEnum;
import com.cky.service.UserService;
import com.cky.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理消息的handler
 * TextWebSocketFrame:在netty中，是用于websocket专门处理文本的对象，frames是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理所有客户端的channel
    private static ChannelGroup users =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        //获取客户端传输过来的消息
        String content = textWebSocketFrame.text();

        Channel currentChannel = channelHandlerContext.channel();

        //1.获取客户端发来的消息
        DataContent dataContent = JsonUtils.jsonToPojo(content,DataContent.class);
        Integer action = dataContent.getAction();
        //2.判断消息的类型，根据不同的类型来处理不同的业务

        if(action == MsgActionEnum.CONNECT.type){
            // 2.1 当webSocket 第一次open的时候，初始化channel，把用户的channel和userId关联起来
            String senderId = dataContent.getChatMsg().getSenderId();
            // 2.2 判断userId和channel是否已经关联，如果是，则关闭已关联的channel
            Channel channel = UserChannelRel.get(senderId);
            if( channel != null){
                channel = users.find(channel.id());
                if(channel != null){

                    DataContent data = new DataContent();
                    data.setAction(MsgActionEnum.FORCED_EXSIT.type);
                    channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(data)));
                    channel.close();
                    users.remove(channel);
                    System.out.println("重新登录，userId:"+senderId);
                }
            }
            UserChannelRel.put(senderId,currentChannel);

            //测试
            for (Channel c : users){
                System.out.println(c.id().asLongText());
            }
            UserChannelRel.output();
        }else if(action == MsgActionEnum.CHAT.type){
            // 2.2 聊天类型的消息，把聊天记录保存到数据库,同时标记消息的签收状态【未签收】
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String receiverId = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();

            //保存消息到数据库，并且标记为未签收
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            String msgId = null;
            try {
                msgId = userService.saveMsg(chatMsg);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("表情图："+msgText);
            }
            chatMsg.setMsgId(msgId);

            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);
            //发送消息
            //从全局用户Channel关系中获取接收方的channel
            Channel receiverChannel = UserChannelRel.get(receiverId);
            if(receiverChannel == null){
                //TODO channel 为空代表用户离线，消息推送（Jpush，个推，小米推送）

            }else {
                //当 receiverChannel 不为康的时候，从ChannelGroup去查找对应的channel是否存在
                Channel findChannel = users.find(receiverChannel.id());
                if(findChannel != null){
                    //用户在线
                    receiverChannel.writeAndFlush(
                            new TextWebSocketFrame(
                                    JsonUtils.objectToJson(dataContentMsg)));
                }else {
                    //用户离线 TODO 推送消息
                }
            }

        }else if(action == MsgActionEnum.SIGNED.type){
            // 2.3 签收消息类型 ，针对具体消息进行签收，修改数据库中对应消息的签收状态【已签收】
            UserService userService = (UserService) SpringUtil.getBean("userServiceImpl");
            //扩展字段在Signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdStr = dataContent.getExtand();
            String[] msgIds = msgIdStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for(String msgId : msgIds){
                if(StringUtils.isNotBlank(msgId)){
                    msgIdList.add(msgId);
                }
            }
            System.out.println(msgIdList.toString());
            if(msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0){
                //批量签收
                userService.updateMsgSigned(msgIdList);
            }

        }else if(action == MsgActionEnum.KEEPALIVE.type){
            // 2.4 心跳类型的消息
        }


    }

    /**
     * 当客户端连接服务器之后,
     * 获取客户端的channel，并放到ChannelGroup中进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为："+ channelId);
        users.remove(ctx.channel());
//        System.out.println("客户端端开连接，id为："+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //发生异常之后关闭channel，随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
