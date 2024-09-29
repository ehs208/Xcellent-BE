package com.leets.xcellentbe.domain.chatroom.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.chatroom.domain.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
}
