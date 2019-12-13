package com.relipa.religram.messages.websocket;

import com.relipa.religram.util.ChatUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromHttpRequest(request).build().getQueryParams();
        List<String> userIds = parameters.get("userId");
        if (!userIds.isEmpty()) {
            return new StompPrincipal(ChatUtils.getUserIdentity(userIds.get(0)));
        }

        return super.determineUser(request, wsHandler, attributes);
    }
}
