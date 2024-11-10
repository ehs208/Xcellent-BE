package com.leets.xcellentbe.domain.article.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.article.exception.DeleteForbiddenException;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.articleMedia.domain.repository.ArticleMediaRepository;
import com.leets.xcellentbe.domain.comment.domain.repository.CommentRepository;
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
	private final ArticleMediaRepository articleMediaRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	private final HashtagService hashtagService;
	private final S3UploadMediaService s3UploadMediaService;
	private final JwtService jwtService;

	public List<ArticlesResponseDto> getArticles(String customId, boolean mediaOnly) {
		User user = getUser(customId);
		List<ArticlesWithMediaDto[]> posts = getPosts(user);

		Map<Article, List<String>> groupedPosts = groupPostsByFilePath(posts, mediaOnly);

		return groupedPosts.entrySet().stream()
			.map(entry -> ArticlesResponseDto.of(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 게시글 파일 경로 그룹화 (미디어 필터링 조건 추가)
	private Map<Article, List<String>> groupPostsByFilePath(List<ArticlesWithMediaDto[]> posts, boolean mediaOnly) {
		return posts.stream()
			.flatMap(Arrays::stream)
			.filter(post -> !mediaOnly || post.getFilePath() != null) // 미디어 있는 경우만 필터링
			.collect(Collectors.groupingBy(
				ArticlesWithMediaDto::getArticle,
				Collectors.mapping(ArticlesWithMediaDto::getFilePath, Collectors.toList())
			));
	}

	// 유저 정보로 게시글 조회
	private List<ArticlesWithMediaDto[]> getPosts(User user) {
		return articleRepository.findPostsByWriter(user);
	}

	// 유저 정보 조회
	private User getUser(String customId) {
		return userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
	}

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
		List<ArticleMedia> mediaList = saveArticleMedia(mediaFiles, newArticle);
		if (!mediaList.isEmpty()) {
			newArticle.addMedia(mediaList);
		}

		return ArticleCreateResponseDto.from(articleRepository.save(newArticle));
	}

	//미디어 생성
	public List<ArticleMedia> saveArticleMedia(List<MultipartFile> mediaFiles, Article article) {
		List<ArticleMedia> articleMediaList = new ArrayList<>();

		for (MultipartFile multipartFile : mediaFiles) {
			String fileUrl = s3UploadMediaService.upload(multipartFile, "article");
			ArticleMedia media = ArticleMedia.createArticleMedia(article, fileUrl);
			articleMediaRepository.save(media);
			articleMediaList.add(media);
		}
		return articleMediaList;
	}

	//게시글 삭제 (상태 변경)
	public void deleteArticle(HttpServletRequest request, UUID articleId) {
		User user = getUser(request);

		Article targetArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);

		if (!(targetArticle.getWriter().getUserId().equals(user.getUserId()))) {
			throw new DeleteForbiddenException();
		} else {
			targetArticle.deleteArticle();
			deleteMediaByArticle(articleId);
			hashtagService.deleteHashtags(targetArticle);
		}
	}

	//미디어 삭제
	public void deleteMediaByArticle(UUID articleId) {
		List<ArticleMedia> mediaList = articleMediaRepository.findByArticle_ArticleId(articleId);

		if (!(mediaList.isEmpty())) {
			for (ArticleMedia media : mediaList) {
				s3UploadMediaService.removeFile(media.getFilePath(), "articles/");
				media.deleteMedia();
			}
		}
	}

	//게시글 단건 조회
	public ArticleResponseDto getArticle(HttpServletRequest request, UUID articleId) {

		User user = getUser(request);

		Article targetArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);

		targetArticle.updateViewCount();
		boolean isOwner = targetArticle.getWriter().getUserId().equals(user.getUserId());

		return ArticleResponseDto.from(targetArticle, isOwner);
	}

	//게시글 전체 조회
	public List<ArticleResponseDto> getArticles(HttpServletRequest request, LocalDateTime cursor, int size) {

		User user = getUser(request);

		Pageable pageable = PageRequest.of(0, size);

		List<Article> articles = cursor == null ?
			articleRepository.findRecentArticles(pageable) : // 처음 로드 시
			articleRepository.findRecentArticles(cursor, pageable);

		return articles
			.stream()
			.map(article -> ArticleResponseDto.from(article, article.getWriter().getUserId().equals(user.getUserId())))
			.collect(Collectors.toList());
	}

	//리포스트 작성 (인용 x, 단순)
	public ArticleCreateResponseDto rePostArticle(HttpServletRequest request, UUID articleId) {
		User writer = getUser(request);

		//원본 조회
		Article repostedArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);
		Article newArticle = Article.createArticle(writer, repostedArticle.getContent());
		repostedArticle.addRepost(newArticle);
		repostedArticle.plusRepostCount();

		return ArticleCreateResponseDto.from(articleRepository.save(newArticle));
	}

	//리포스트 삭제
	public void deleteRepost(HttpServletRequest request, UUID articleId) {

		User user = getUser(request);
		Article targetArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);
		if (!(targetArticle.getWriter().getUserId().equals(user.getUserId()))) {
			throw new DeleteForbiddenException();
		} else {
			targetArticle.deleteArticle();
			targetArticle.getRePost().minusRepostCount();
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
