package com.leets.xcellentbe.domain.follow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.follow.domain.Follow;
import com.leets.xcellentbe.domain.follow.domain.repository.FollowRepository;
import com.leets.xcellentbe.domain.follow.dto.FollowInfoResponseDto;
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

	private static final int PAGE_SIZE = 10;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final FollowRepository followRepository;

	// 팔로우
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

	// 언팔로우
	public void unfollowUser(FollowRequestDto requestDto, HttpServletRequest request) {
		User user = getUser(request);
		User targetUser = userRepository.findByCustomId(requestDto.getCustomId())
			.orElseThrow(UserNotFoundException::new);

		Follow follow = followRepository.findByFollowerAndFollowing(user, targetUser)
			.orElseThrow(FollowOperationError::new);

		followRepository.delete(follow);
	}

	// 팔로잉 목록 조회
	public Page<FollowInfoResponseDto> getFollowingList(String customId, int pageNo) {
		User user = findUserByCustomId(customId);
		Pageable pageable = createPageable(pageNo);

		return followRepository.findByFollower(user, pageable)
			.map(FollowInfoResponseDto::from);
	}

	// 팔로워 목록 조회
	public Page<FollowInfoResponseDto> getFollowerList(String customId, int pageNo) {
		User user = findUserByCustomId(customId);
		Pageable pageable = createPageable(pageNo);

		return followRepository.findByFollowing(user, pageable)
			.map(FollowInfoResponseDto::from);
	}

	// 커스텀아이디로 유저 검색
	private User findUserByCustomId(String customId) {
		return userRepository.findByCustomId(customId)
			.orElseThrow(UserNotFoundException::new);
	}

	// 페이지네이션 객체 생성
	private Pageable createPageable(int pageNo) {
		return PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "following"));
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
