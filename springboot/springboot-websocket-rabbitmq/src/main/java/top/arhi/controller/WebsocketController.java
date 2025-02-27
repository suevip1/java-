package top.arhi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.arhi.entity.ChatMessage;
import top.arhi.service.ChatService;
import top.arhi.util.JsonUtil;
import top.arhi.util.RedisUtils;

import java.security.Principal;
import java.util.Set;

@Controller
public class WebsocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${redis.channel.msgAlone}")
    private String msgAlone;

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;


    @ResponseBody
    @GetMapping("/getOnlineUsers")
    public void getOnlineUsersNum(){
        Set<String> resultSet = redisTemplate.opsForSet().members(onlineUsers);
        System.out.println("在线人数 :"+resultSet.size()+"\n");
        System.out.println("在线用户："+resultSet.toString());


    }
    @ResponseBody
    @GetMapping("/sendToOne")
    public void sendToOne(@RequestParam("uid") String uid,@RequestParam("content") String content ){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent(content);
        chatMessage.setTo(uid);
        chatMessage.setSender("系统消息");
        rabbitTemplate.convertAndSend("stomp", "topic.mine", JsonUtil.parseObjToJson(chatMessage));
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        try {
            System.out.println("---------------群发消息----------");
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息给所有人
    @MessageMapping("/chat.sendMessageTest")
    public void sendMessageAlone(@Payload ChatMessage chatMessage, Principal principal) {
        try {
            System.out.println("---------------单发消息----------");
            System.out.println("模拟单对单发送消息！");
            rabbitTemplate.convertAndSend("stomp", "topic.mine", JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    //用户上线通知放到公共队列
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage) {
        LOGGER.info("有用户加入到了websocket 消息室" + chatMessage.getSender());
        try {
            redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
            System.out.println(chatMessage.toString());
            rabbitTemplate.convertAndSend("stomp", "topic.mine", JsonUtil.parseObjToJson(chatMessage));

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    @ResponseBody
    @GetMapping("/sendToAll")
    public void sendToAll(@RequestParam("content") String content ){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent(content);
        chatMessage.setTo("all");
        chatMessage.setSender("系统消息");
        rabbitTemplate.convertAndSend("stomp", "topic.mine", JsonUtil.parseObjToJson(chatMessage));
    }


}
