package com.leets.xcellentbe.domain.dm.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.chatroom.domain.Chatroom;
import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DM extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID DMId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatRoom;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "participant_id")
	private User participant;

	@NotNull
	@Column
	private String message;

	@NotNull
	@Column
	private DeletedStatus deletedStatus;
}
