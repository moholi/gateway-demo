package com.example.demoservice.rest;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demoservice.config.WebSocketConfig;


@Controller
public class SocketMessageController {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private @Autowired SimpMessagingTemplate template;
	
	@MessageMapping("/sendMsg")
    public void sendMsg(Principal principal, String message) {
		log.debug("message of client:{}",message);
	}
	
	@GetMapping("hello")
	@ResponseBody
	public boolean hello() {
		return true;
	}
	
	@SubscribeMapping("/queue/{subscribeId}/msg")
    public void subscribeInit(@DestinationVariable String subscribeId) {
		
		template.convertAndSendToUser(subscribeId, WebSocketConfig.DESTINATION, "ni hao");
    }
}
