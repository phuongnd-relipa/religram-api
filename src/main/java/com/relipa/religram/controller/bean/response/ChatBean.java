package com.relipa.religram.controller.bean.response;

import com.relipa.religram.controller.bean.AbstractBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatBean extends AbstractBean {
    private Long chatUserId;
    private String chatUserName;
    private String lastMessage;
    private Long lastMessageFrom;
}
