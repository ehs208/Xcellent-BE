package com.leets.xcellentbe.domain.dm.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.dm.dto.DMDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
	private final RedisTemplate<String, Object> redisTemplate;

	public void publish(ChannelTopic topic, DMDto dmDto) {
		redisTemplate.convertAndSend(topic.getTopic(), dmDto);
	}
}
