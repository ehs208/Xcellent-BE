package com.leets.xcellentbe.domain.user.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

	private String email;
	private String customId;
	private String userName;
	private String password;
	private String phoneNumber;
	private LocalDate userBirthDay;

}
