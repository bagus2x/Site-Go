package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.Connection;
import id.co.mii.sitego.model.request.ConnectionRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class MeetingController {
    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Connection connection = (Connection) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("connection");

        String destination = String.format("/topic/meeting/%s/disconnect", connection.getMeetingId());
        messageSendingOperations.convertAndSend(destination, connection);
    }

    @MessageMapping("/meeting/{meetingId}/connect")
    @SendTo("/topic/meeting/{meetingId}/connect")
    public Connection connect(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String meetingId, ConnectionRequest request) {
        Connection connection = Connection.builder()
            .id(request.getId())
            .name(request.getName())
            .meetingId(meetingId)
            .build();

        Map<String, Object> sessionAttributes = Objects.requireNonNull(headerAccessor.getSessionAttributes());
        sessionAttributes.put("meetingId", meetingId);
        sessionAttributes.put("connection", connection);

        return connection;
    }

    @GetMapping("/meeting/{meetingId}")
    public String meetingView(@PathVariable String meetingId, Model model) {
        model.addAttribute("meetingId", meetingId);
        return "meeting/meeting";
    }
}
