package com.leets.xcellentbe.domain.chatroom.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.shared.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ChatroomId;

	@Column(length = 50)
	private String lastParticipantName;

	@Column
	private String lastMessage;
}
