package com.leets.xcellentbe.domain.follow.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.follow.domain.Follow;
import com.leets.xcellentbe.domain.follow.domain.repository.FollowRepository;
import com.leets.xcellentbe.domain.follow.dto.FollowRequestDto;
import com.leets.xcellentbe.domain.follow.exception.FollowOperationError;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class FollowService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final FollowRepository followRepository;

	public void followUser(FollowRequestDto requestDto, HttpServletRequest request) {
		User user = getUser(request);
		User targetUser = userRepository.findByCustomId(requestDto.getCustomId())
			.orElseThrow(UserNotFoundException::new);

		boolean isFollowing = followRepository.findByFollowerAndFollowing(user, targetUser).isPresent();

		if (isFollowing) {
			throw new FollowOperationError();
		}

		Follow follow = Follow.create(user, targetUser);
		followRepository.save(follow);
	}

	public void unfollowUser(FollowRequestDto requestDto, HttpServletRequest request) {
		User user = getUser(request);
		User targetUser = userRepository.findByCustomId(requestDto.getCustomId())
			.orElseThrow(UserNotFoundException::new);

		Follow follow = followRepository.findByFollowerAndFollowing(user, targetUser)
			.orElseThrow(FollowOperationError::new);

		followRepository.delete(follow);
	}

	//JWT 토큰 해독하여 사용자 정보 반환 메소드
	private User getUser(HttpServletRequest request) {
		Optional<User> user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(accessToken -> jwtService.extractEmail(accessToken))
			.flatMap(userRepository::findByEmail);

		if (user.isEmpty()) {
			throw new UserNotFoundException();
		}

		return user.get();
	}
}
