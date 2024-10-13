package com.leets.xcellentbe.global.auth.email;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCheckDto {
	@Email
	private String email;
	private String authNum;
}
