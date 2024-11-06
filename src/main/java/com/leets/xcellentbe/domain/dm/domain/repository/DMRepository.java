package com.leets.xcellentbe.domain.dm.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.dm.domain.DM;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

public interface DMRepository extends JpaRepository<DM, UUID> {

	List<DM> findTop100ByChatRoomAndDeletedStatusNotOrderByCreatedAtAsc(ChatRoom chatRoom, DeletedStatus deletedStatus);

	DM findTopByChatRoomAndDeletedStatusNotOrderByCreatedAtDesc(ChatRoom chatRoom, DeletedStatus deletedStatus);
}
