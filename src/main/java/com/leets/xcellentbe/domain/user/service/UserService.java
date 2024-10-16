package com.leets.xcellentbe.domain.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.exception.UserAlreadyExistsException;

import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;

import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import com.leets.xcellentbe.domain.user.dto.UserProfileResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public String register(UserSignUpRequestDto userSignUpRequestDto) {

		if (userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException();
		}
		if (userRepository.findByCustomId(userSignUpRequestDto.getCustomId()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = User.create(userSignUpRequestDto.getCustomId(), userSignUpRequestDto.getEmail(), userSignUpRequestDto.getUserName(),
				userSignUpRequestDto.getPassword(), userSignUpRequestDto.getPhoneNumber(), userSignUpRequestDto.getUserBirthYear(), userSignUpRequestDto.getUserBirthDay(), userSignUpRequestDto.getUserBirthMonth());

		user.passwordEncode(passwordEncoder);
		userRepository.save(user);

		return "회원가입이 완료되었습니다.";
	}

	public UserProfileResponseDto getProfile(HttpServletRequest request) {
		Optional<User> user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(accessToken -> jwtService.extractEmail(accessToken))
			.flatMap(email -> userRepository.findByEmail(email));

		return UserProfileResponseDto.builder()
			.customId(user.get().getCustomId())
			.email(user.get().getEmail())
			.userName(user.get().getUserName())
			.phoneNumber(user.get().getPhoneNumber())
			.userBirthYear(user.get().getUserBirthYear())
			.userBirthDay(user.get().getUserBirthDay())
			.userBirthMonth(user.get().getUserBirthMonth())
			.build();
	}

}
