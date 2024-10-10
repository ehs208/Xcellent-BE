package com.leets.xcellentbe.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginRequestDto {
	private String email;
	private String password;

}
