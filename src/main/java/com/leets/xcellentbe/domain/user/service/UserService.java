package com.leets.xcellentbe.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.user.User;
import com.leets.xcellentbe.domain.user.exception.UserAlreadyExistsException;

import com.leets.xcellentbe.domain.user.repository.UserRepository;
import com.leets.xcellentbe.domain.user.dto.UserSignUpDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public String register(UserSignUpDto userSignUpDto) {

		if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException();
		}
		if (userRepository.findByCustomId(userSignUpDto.getCustomId()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = User.create(userSignUpDto.getCustomId(), userSignUpDto.getEmail(), userSignUpDto.getUserName(),
				userSignUpDto.getPassword(), userSignUpDto.getPhoneNumber(), userSignUpDto.getDescription());

		user.passwordEncode(passwordEncoder);
		userRepository.save(user);

		return "회원가입이 완료되었습니다.";
	}

}
