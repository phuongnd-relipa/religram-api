package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.ChatBean;
import com.relipa.religram.entity.Message;
import com.relipa.religram.exceptionhandler.SendMessageOwnerException;
import com.relipa.religram.messages.websocket.ChatMessage;
import com.relipa.religram.service.ChatService;
import com.relipa.religram.service.MessageService;
import com.relipa.religram.util.ChatUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("v1/chat")
@Api(tags = {"chat"})
public class ChatController {

    @Autowired
    SimpMessageSendingOperations messageTemplate;

    @Autowired
    ChatService chatService;

    @Autowired
    MessageService messageService;

    /*@GetMapping("/ws")
    public String chat() {
        return "socket_chat";
    }*/

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendTopicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @PostMapping("/directMessage")
    @ApiOperation(value = "${chat.directMessage.post.value}", notes = "${chat.directMessage.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = ChatMessage.class)})
    public ResponseEntity<ChatMessage> directMessage(@ApiParam(value = "${chat.directMessage.post.param.receiver}", required = true) @RequestParam Long receiverId,
                                                     @ApiParam(value = "${chat.directMessage.post.param.message}", required = true) @RequestParam String message,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        ChatMessage chatMessage = chatService.sendDirectMessage(receiverId, userDetails, message);
        if (!chatMessage.getSender().equals(chatMessage.getReceiver())) {
            // Socket send message to client's queue
            messageTemplate.convertAndSendToUser(ChatUtils.getUserIdentity(chatMessage.getSender()), "/queue/reply", chatMessage);
            messageTemplate.convertAndSendToUser(ChatUtils.getUserIdentity(chatMessage.getReceiver()), "/queue/reply", chatMessage);

            // Save message
            Message messageToSave = new Message();
            messageToSave.setFromId(Long.valueOf(chatMessage.getSender()));
            messageToSave.setToId(Long.valueOf(chatMessage.getReceiver()));
            messageToSave.setContent(chatMessage.getContent());
            messageService.save(messageToSave);
        } else {
            // Error send message to owner
            throw new SendMessageOwnerException("Can not send message to owner!");
        }
        return ok(chatMessage);
    }

    @GetMapping("/conversation")
    @ApiOperation(value = "${chat.conversation.get.value}", notes = "${chat.conversation.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = Message.class, responseContainer = "List")})
    public ResponseEntity<List<Message>> getConversation(@ApiParam(value = "${chat.conversation.get.param.user-1}", required = true) @RequestParam Long userId1,
                                                         @ApiParam(value = "${chat.conversation.get.param.user-2}", required = true) @RequestParam Long userId2,
                                                         @ApiParam(value = "${chat.conversation.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page) {
        List<Message> messages = messageService.getConversation(userId1, userId2, page);
        return ok(messages);
    }

    @GetMapping("/{userId}/chat-list")
    @ApiOperation(value = "${chat.user-list.get.value}", notes = "${chat.user-list.get.notes}")
    public ResponseEntity<List<ChatBean>> getChatList(@ApiParam(value = "${chat.user-list.get.param.userId}", required = true) @PathVariable Long userId) {
        List<ChatBean> results = messageService.getChatList(userId);
        return ok(results);
    }
}
