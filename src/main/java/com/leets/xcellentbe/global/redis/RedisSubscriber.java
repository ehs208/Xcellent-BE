package com.leets.xcellentbe.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.xcellentbe.domain.dm.dto.DMDto;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessageSendingOperations simpMessageSendingOperations;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
			DMDto dmDto = objectMapper.readValue(publishMessage, DMDto.class);
			simpMessageSendingOperations.convertAndSend("/sub/chat/" + dmDto.chatRoomId(), dmDto);
		} catch (JsonMappingException e) {
			throw new RuntimeException();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
