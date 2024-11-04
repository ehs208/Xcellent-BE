package com.leets.xcellentbe.domain.dm.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
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
public class DM extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "BINARY(16)")
	private UUID DMId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatRoom_id")
	private ChatRoom chatRoom;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@NotNull
	@Column
	private String message;

	@NotNull
	@Column
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	public static DM create(User sender, User receiver, ChatRoom chatRoom, String message) {
		return DM.builder()
			.sender(sender)
			.receiver(receiver)
			.chatRoom(chatRoom)
			.message(message)
			.build();
	}

	@Builder
	private DM(User sender, User receiver, ChatRoom chatRoom, String message) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.chatRoom = chatRoom;
		this.message = message;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public void delete() {
		this.deletedStatus = DeletedStatus.DELETED;
	}
}
