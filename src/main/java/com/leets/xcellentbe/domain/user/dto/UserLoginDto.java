package com.leets.xcellentbe.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginDto {
	private String email;
	private String password;

}
