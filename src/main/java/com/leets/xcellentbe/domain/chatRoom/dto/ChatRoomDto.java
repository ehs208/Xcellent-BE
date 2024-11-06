package com.leets.xcellentbe.domain.chatRoom.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leets.xcellentbe.domain.dm.dto.request.DMRequest;
import com.leets.xcellentbe.domain.user.domain.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatRoomDto(UUID chatRoomId, Long senderId, Long receiverId) implements Serializable {

    @Serial
    private static final long serialVersionUID = 6494678977089006639L;

    public static ChatRoomDto of(DMRequest dmRequest, User user) {
        return new ChatRoomDto(null, user.getUserId(), dmRequest.receiverId());
    }

    public static ChatRoomDto of(UUID chatRoomId, User sender, User receiver) {
        return new ChatRoomDto(chatRoomId, sender.getUserId(), receiver.getUserId());
    }
}
