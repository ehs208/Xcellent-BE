package com.leets.xcellentbe.domain.article.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleDeleteRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.articleMedia.dto.ArticleMediaRequestDto;
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
	public ArticleResponseDto createArticle(HttpServletRequest request, ArticleCreateRequestDto articleCreateRequestDto, List<MultipartFile> mediaFiles) {
		User user = getUser(request);
		Article newArticle;

		if(articleCreateRequestDto.getRePostId() != null) {
			newArticle = rePostArticle(user, articleCreateRequestDto);
		}
		else
			newArticle=Article.createArticle(user, articleCreateRequestDto.getContent());

		//해시태그 처리
		List<Hashtag> hashtags = hashtagService.extractAndSaveHashtags(articleCreateRequestDto.getContent());
		newArticle.addHashtag(hashtags);

		//이미지 파일 처리
		List<ArticleMedia> mediaList = articleMediaService.saveArticleMedia(mediaFiles, newArticle);
		newArticle.addMedia(mediaList);

		return ArticleResponseDto.from(articleRepository.save(newArticle));
	}
	//게시글 고정 -> 하나만 되도록 로직 검사 변경 로직 추가
	public void pinArticle(UUID articleId, Long accountId) {

		Optional<Article> nowPinnedArticle = articleRepository.findByWriterIdAndIsPinned(accountId, true);
		nowPinnedArticle.ifPresent(pinnedArticle -> {
			pinnedArticle.unPinArticle();
			articleRepository.save(pinnedArticle);
		});

		Article article = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);

		article.pinArticle();

		articleRepository.save(article);
	}
	//게시글 수정
	public void updateArticle(ArticleRequestDto articleRequestDto) {
		Article article = articleRepository.findById(articleRequestDto.getArticleId())
			.orElseThrow(ArticleNotFoundException::new);

		article.updateArticle(articleRequestDto.getContent());

		//해시태그 수정
		if (!articleRequestDto.getHashtags().isEmpty()) {
			articleRequestDto.getHashtags().clear();
			List<Hashtag> newHashtags = hashtagService.extractAndSaveHashtags(articleRequestDto.getContent());
			article.addHashtag(newHashtags);
		}
		//미디어 파일 추가
		if (articleRequestDto.getNewMediaFiles() != null && !articleRequestDto.getNewMediaFiles().isEmpty()) {
			List<ArticleMedia> mediaList = articleMediaService.saveArticleMedia(articleRequestDto.getNewMediaFiles(), article);
			article.addMedia(mediaList);
		}

		//미디어 파일 단건 삭제
		if (articleRequestDto.getDeleteMediaIds() != null && !articleRequestDto.getDeleteMediaIds().isEmpty()) {
			for (UUID mediaId : articleRequestDto.getDeleteMediaIds()) {
				ArticleMediaRequestDto requestDto = new ArticleMediaRequestDto();
				removeMedia(requestDto);
			}
		}
		articleRepository.save(article);
	}
	//게시글에 미디어 삭제
	public void removeMedia(ArticleMediaRequestDto requestDto) {
		articleMediaService.deleteArticleMedia(requestDto);
	}

	//게시글 삭제 (소프트 삭제 =>제상태 변경)
	public void deleteArticle(ArticleDeleteRequestDto articleDeleteRequestDto, ArticleMediaRequestDto requestDto){
		Article article = articleRepository.findById(articleDeleteRequestDto.getArticleId())
			.orElseThrow(ArticleNotFoundException::new);

		article.deleteArticle();
		articleMediaService.deleteAllMediaByArticle(requestDto);
	}

	//게시글 조회
	public ArticleResponseDto getArticle(ArticleRequestDto articleRequestDto) {
		Article article = articleRepository.findById(articleRequestDto.getArticleId())
			.orElseThrow(ArticleNotFoundException::new);

		return ArticleResponseDto.from(article);
	}

	public Article rePostArticle(User user, ArticleCreateRequestDto articleCreateRequestDto) {
		//원본 조회
		Article repostedArticle = articleRepository.findById(articleCreateRequestDto.getRePostId())
			.orElseThrow(ArticleNotFoundException::new);

		//리포스트(인용)
		if (Boolean.TRUE.equals(articleCreateRequestDto.getIsQuoteRepost())) {
			Article article = Article.createArticle(user, articleCreateRequestDto.getContent());
			article.addRepost(repostedArticle);
			return article;
		}
		//리포스트(단순)
		else {
			Article article = Article.createArticle(user, repostedArticle.getContent());
			article.addRepost(repostedArticle);
			return article;
		}
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
