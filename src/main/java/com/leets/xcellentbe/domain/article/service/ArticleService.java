package com.leets.xcellentbe.domain.article.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticleDeleteRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleRepostDto;
import com.leets.xcellentbe.domain.article.dto.ArticleRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
import com.leets.xcellentbe.domain.article.dto.DeleteRepostRequestDto;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.articleMedia.service.ArticleMediaService;
import com.leets.xcellentbe.domain.hashtag.HashtagService.HashtagService;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final UserRepository userRepository;
	private final HashtagService hashtagService;
	private final ArticleMediaService articleMediaService;
	private final JwtService jwtService;

	//게시글 작성
	public ArticleCreateResponseDto createArticle(HttpServletRequest request,
											ArticleCreateRequestDto articleCreateRequestDto,
											List<MultipartFile> mediaFiles) {
		User writer = getUser(request);
		String content = articleCreateRequestDto.getContent();
		//게시글 생성
		Article newArticle = Article.createArticle(writer, content);
		//해시태그 처리
		List<Hashtag> hashtags = hashtagService.extractAndSaveHashtags(newArticle, content);
		if (!hashtags.isEmpty()) {
			newArticle.addHashtag(hashtags);
		}
		//이미지 파일 처리
		List<ArticleMedia> mediaList = articleMediaService.saveArticleMedia(mediaFiles, newArticle);
		if (!mediaList.isEmpty()) {
			newArticle.addMedia(mediaList);
		}

		return ArticleCreateResponseDto.from(articleRepository.save(newArticle));
	}

	//게시글 삭제 (상태 변경)
	public void deleteArticle(ArticleDeleteRequestDto articleDeleteRequestDto){
		UUID targetId = articleDeleteRequestDto.getArticleId();

		Article targetArticle = articleRepository.findById(targetId)
			.orElseThrow(ArticleNotFoundException::new);

		targetArticle.deleteArticle();
		articleMediaService.deleteMediaByArticle(targetArticle);
	}

	//게시글 단건 조회
	public ArticleResponseDto getArticle(ArticleRequestDto articleRequestDto) {
		UUID targetId = articleRequestDto.getArticleId();

		Article article = articleRepository.findById(targetId)
			.orElseThrow(ArticleNotFoundException::new);

		return ArticleResponseDto.from(article);
	}

	//게시글 전체 조회
	public List<ArticleResponseDto> getArticles(Long cursor, int size) {

		List<Article> articles = cursor == null ?
			articleRepository.findByCreatedAtDesc(size) : // 처음 로드 시
			articleRepository.findByCursorCreatedAtDesc(cursor, size);

		return articles
			.stream()
			.map(ArticleResponseDto::from)
			.collect(Collectors.toList());
	}

	//리포스트 작성 (인용 x, 단순)
	public ArticleCreateResponseDto rePostArticle(HttpServletRequest request,
											ArticleRepostDto articleRepostRequestDto) {
		User writer = getUser(request);
		UUID targetId = articleRepostRequestDto.getRePostId();
		//원본 조회
		Article repostedArticle = articleRepository.findById(targetId)
			.orElseThrow(ArticleNotFoundException::new);

		Article newArticle = Article.createArticle(writer, repostedArticle.getContent());
		repostedArticle.addRepost(repostedArticle);

		return ArticleCreateResponseDto.from(articleRepository.save(newArticle));
	}

	//리포스트 삭제
	public void deleteRepost(DeleteRepostRequestDto deleteRepostRequestDto) {
		UUID targetId = deleteRepostRequestDto.getRePostId();

		Article targetArticle = articleRepository.findById(targetId)
			.orElseThrow(ArticleNotFoundException::new);

		targetArticle.deleteArticle();
	}

	//JWT 토큰 기반 사용자 정보 반환 메소드
	private User getUser(HttpServletRequest request) {
		User user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(jwtService::extractEmail)
			.flatMap(userRepository::findByEmail)
			.orElseThrow(UserNotFoundException::new);

		return user;
	}
}
