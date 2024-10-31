package com.leets.xcellentbe.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.post.domain.repository.PostRepository;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.dto.UserProfileRequestDto;
import com.leets.xcellentbe.domain.user.dto.UserProfileResponseDto;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;
import com.leets.xcellentbe.domain.user.exception.UserAlreadyExistsException;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final S3UploadService s3UploadService;
	private final PostRepository postRepository;

	// 회원가입 메소드
	public String register(UserSignUpRequestDto userSignUpRequestDto) {

		if (userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException();
		}
		if (userRepository.findByCustomId(userSignUpRequestDto.getCustomId()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = User.create(userSignUpRequestDto.getCustomId(), userSignUpRequestDto.getEmail(),
			userSignUpRequestDto.getUserName(),
			userSignUpRequestDto.getPassword(), userSignUpRequestDto.getPhoneNumber(),
			userSignUpRequestDto.getUserBirthYear(), userSignUpRequestDto.getUserBirthDay(),
			userSignUpRequestDto.getUserBirthMonth());

		user.passwordEncode(passwordEncoder);
		userRepository.save(user);

		return "회원가입이 완료되었습니다.";
	}

	// 본인 정보 조회 메소드
	public UserProfileResponseDto getProfile(HttpServletRequest request) {
		User user = getUser(request);
		return UserProfileResponseDto.from(user);
	}

	// 특정 사용자 정보 조회 메소드
	public UserProfileResponseDto getProfileWithoutToken(String customId) {
		User user = userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
		return UserProfileResponseDto.from(user);
	}

	// 사용자 정보 수정 메소드
	public void updateProfile(HttpServletRequest request, UserProfileRequestDto userProfileRequestDto) {
		User user = getUser(request);
		user.updateProfile(userProfileRequestDto.getUserName(), userProfileRequestDto.getPhoneNumber(),
			userProfileRequestDto.getCustomId(), userProfileRequestDto.getUserBirthYear(),
			userProfileRequestDto.getUserBirthDay(), userProfileRequestDto.getUserBirthMonth(),
			userProfileRequestDto.getDescription(), userProfileRequestDto.getWebsiteUrl(),
			userProfileRequestDto.getLocation());
	}

	// 프로필 이미지 변경 메소드
	public String updateProfileImage(MultipartFile multipartFile, HttpServletRequest request) {
		User user = getUser(request);
		String priorUrl = user.getProfileImageUrl();
		String url = s3UploadService.upload(multipartFile, "profile-image");
		user.updateProfileImage(url);
		if (priorUrl != null) {
			s3UploadService.removeFile(priorUrl, "profile-image/");
		}

		return url;
	}

	// 배경 이미지 변경 메소드
	public String updateBackgroundProfileImage(MultipartFile multipartFile, HttpServletRequest request) {
		User user = getUser(request);
		String priorUrl = user.getBackgroundProfileImageUrl();
		String url = s3UploadService.upload(multipartFile, "background-image");
		user.updateBackgroundImage(url);

		if (priorUrl != null) {
			s3UploadService.removeFile(priorUrl, "background-image/");
		}

		return url;
	}

	//JWT 토큰 해독하여 사용자 정보 반환 메소드
	private User getUser(HttpServletRequest request) {
		User user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(jwtService::extractEmail)
			.flatMap(userRepository::findByEmail)
			.orElseThrow(UserNotFoundException::new);

		return user;
	}

}
