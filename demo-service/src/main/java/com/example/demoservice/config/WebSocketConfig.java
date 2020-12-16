package com.example.demoservice.config;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static String END_POINT = "/api/message/ws/any-socket/";
	public static String ACCOUNT_ID = "accountId";
	public static String PROFIX = "/api/message/ws";
	public static String QUEUE = "/api/message/ws/queue";
	public static String TOPIC = "/topic";
	public static String DESTINATION = "msg";
	
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint(END_POINT)
        	.setAllowedOrigins("*")
        	.addInterceptors(new HandshakeInterceptor() {
                @Override
                public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                	return true;
                }

                @Override
                public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                }
            })
        	.setHandshakeHandler(new DefaultHandshakeHandler() {
            @Nullable
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
				return new Principal() {
					
					@Override
					public String getName() {
						return "ni hao";
					}
				};
            }
        }).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
		messageBrokerRegistry.setApplicationDestinationPrefixes(PROFIX);
		messageBrokerRegistry.enableSimpleBroker(QUEUE, TOPIC);
        messageBrokerRegistry.setUserDestinationPrefix(QUEUE);
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                    	if(null != session.getPrincipal()) {
                    		String username = session.getPrincipal().getName();
                            log.debug("online:" + username);
                    	}
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
                            throws Exception {
                    	if(null != session.getPrincipal()) {
                    		String username = session.getPrincipal().getName();
                            log.debug("offline:" + username);
                    	}
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }
}
