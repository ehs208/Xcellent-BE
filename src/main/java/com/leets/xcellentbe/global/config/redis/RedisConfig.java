package com.leets.xcellentbe.global.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.leets.xcellentbe.domain.dm.dto.DMDto;

@Configuration
public class RedisConfig {

	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, DMDto> redisTemplateMessage(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, DMDto> redisTemplateMessage = new RedisTemplate<>();
		redisTemplateMessage.setConnectionFactory(connectionFactory);
		redisTemplateMessage.setKeySerializer(new StringRedisSerializer());
		redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		return redisTemplateMessage;
	}
}
