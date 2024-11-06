package com.leets.xcellentbe.domain.chatRoom.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

	List<ChatRoom> findBySenderOrReceiverAndDeletedStatusNot(User sender, User receiver, DeletedStatus deletedStatus);

	Optional<ChatRoom> findByChatRoomIdAndSenderOrChatRoomIdAndReceiverAndDeletedStatusNot(UUID ChatRoomId, User sender,
		UUID ChatRoomId1, User receiver, DeletedStatus deletedStatus);

	ChatRoom findBySenderAndReceiverAndDeletedStatusNot(User sender, User receiver, DeletedStatus deletedStatus);

	Optional<ChatRoom> findByChatRoomIdAndDeletedStatusNot(UUID charRoomId, DeletedStatus deletedStatus);
}
