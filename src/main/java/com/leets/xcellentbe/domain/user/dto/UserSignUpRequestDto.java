package com.leets.xcellentbe.domain.user.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpRequestDto {

	private String email;
	private String customId;
	private String userName;
	private String password;
	private String phoneNumber;
	private int userBirthYear;
	private int userBirthMonth;
	private int userBirthDay;

}
