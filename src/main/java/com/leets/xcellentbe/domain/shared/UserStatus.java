package com.leets.xcellentbe.domain.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
	ACTIVE("ACTIVE"),
	INACTIVE("INACTIVE"),
	DELETED("DELETED");

	private final String userStatus;
}
