package com.leets.xcellentbe.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.exception.UserAlreadyExistsException;

import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public String register(UserSignUpRequestDto userSignUpRequestDto) {

		if (userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException();
		}
		if (userRepository.findByCustomId(userSignUpRequestDto.getCustomId()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = User.create(userSignUpRequestDto.getCustomId(), userSignUpRequestDto.getEmail(), userSignUpRequestDto.getUserName(),
				userSignUpRequestDto.getPassword(), userSignUpRequestDto.getPhoneNumber(), userSignUpRequestDto.getUserBirthDay());

		user.passwordEncode(passwordEncoder);
		userRepository.save(user);

		return "회원가입이 완료되었습니다.";
	}

}
