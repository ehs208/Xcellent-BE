package com.leets.xcellentbe.domain.chatRoom.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

	@Id
	@Column(name = "chatRoom_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID chatRoomId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@Column
	private String lastMessage;

	@NotNull
	@Column
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	public static ChatRoom create(User sender, User receiver) {
		return ChatRoom.builder()
			.sender(sender)
			.receiver(receiver)
			.build();
	}

	@Builder
	private ChatRoom(User sender, User receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public void updateReceiver(User receiver) {
        this.receiver = receiver;
    }

	public void updateLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public void delete() {
		this.deletedStatus = DeletedStatus.DELETED;
	}
}
