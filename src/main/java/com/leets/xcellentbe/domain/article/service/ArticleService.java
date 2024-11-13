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
import com.leets.xcellentbe.domain.article.dto.ArticleStatsDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.articleLike.domain.repository.ArticleLikeRepository;
import com.leets.xcellentbe.domain.comment.domain.Comment;
import com.leets.xcellentbe.domain.comment.dto.CommentStatsDto;
import com.leets.xcellentbe.domain.commentLike.domain.repository.CommentLikeRepository;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.global.error.exception.custom.DeleteForbiddenException;
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
	private final ArticleLikeRepository articleLikeRepository;
	private final CommentLikeRepository commentLikeRepository;
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
		ArticleStatsDto stats = findArticleStats(targetArticle);
		targetArticle.updateViewCount();
		boolean isOwner = targetArticle.getWriter().getUserId().equals(user.getUserId());
		boolean isLiked = articleLikeRepository.existsByArticle_ArticleIdAndUser_UserIdAndDeletedStatus(
			targetArticle.getArticleId(), user.getUserId(), DeletedStatus.NOT_DELETED);

		List<Comment> comments = commentRepository.findAllByArticleAndNotDeleted(targetArticle);
		Map<UUID, CommentStatsDto> replyStatsMap = comments.stream()
			.collect(Collectors.toMap(
				Comment::getCommentId,
				reply -> {
					long likeCount = commentLikeRepository.countLikesByComment(reply);
					long replyCount = commentRepository.countRepliesByComment(reply);
					return CommentStatsDto.from(likeCount, replyCount);
				}
			));
		return ArticleResponseDto.from(targetArticle, isOwner, isLiked, stats, replyStatsMap);
	}

	//게시글 전체 조회
	public List<ArticleResponseDto> getArticles(HttpServletRequest request, LocalDateTime cursor, int size) {

		User user = getUser(request);

		Pageable pageable = PageRequest.of(0, size);

		List<Article> articles = (cursor == null) ?
			articleRepository.findRecentArticles(pageable) : // 처음 로드 시
			articleRepository.findRecentArticles(cursor, pageable);

		List<Comment> comments = articles.stream()
			.flatMap(article -> commentRepository.findAllByArticleAndNotDeleted(article).stream())
			.collect(Collectors.toList());

		Map<UUID, CommentStatsDto> replyStatsMap = comments.stream()
			.collect(Collectors.toMap(
				Comment::getCommentId,
				reply -> {
					long likeCount = commentLikeRepository.countLikesByComment(reply);
					long replyCount = commentRepository.countRepliesByComment(reply);
					return CommentStatsDto.from(likeCount, replyCount);
				}
			));

		return articles
			.stream()
			.map(article -> {
				boolean isOwner = article.getWriter().getUserId().equals(user.getUserId());
				ArticleStatsDto stats = findArticleStats(article);
				boolean isLiked = articleLikeRepository.existsByArticle_ArticleIdAndUser_UserIdAndDeletedStatus(
					article.getArticleId(), user.getUserId(), DeletedStatus.NOT_DELETED);
				return ArticleResponseDto.from(article, isOwner, isLiked, stats, replyStatsMap);
			})
			.collect(Collectors.toList());
	}
	//리포스트 작성 (인용 x, 단순)
	public ArticleCreateResponseDto rePostArticle(HttpServletRequest request, UUID articleId) {
		User writer = getUser(request);

		//원본 조회
		Article repostedArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);
		Article newArticle = Article.createArticle(writer, repostedArticle.getContent());
		newArticle.addRepost(repostedArticle);

		return ArticleCreateResponseDto.from(articleRepository.save(newArticle));
	}

	//리포스트 삭제
	public void deleteRepost(HttpServletRequest request, UUID articleId) {

		User user = getUser(request);
		Article targetArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);
		// 게시글 작성자와 현재 사용자 일치 여부 확인, 리포스트 ID가 있는 경우에만 삭제 가능
		if ((!targetArticle.getWriter().getUserId().equals(user.getUserId())) || (targetArticle.getRePost() == null)) {
			throw new DeleteForbiddenException();
		}
		// 리포스트 삭제 처리
		targetArticle.deleteArticle();
		articleRepository.save(targetArticle);
	}

	public ArticleStatsDto findArticleStats(Article article) {
		long likeCount = articleLikeRepository.countLikesByArticleId(article.getArticleId());
		long commentCount = commentRepository.countCommentsByArticle(article);
		long repostCount = articleRepository.countReposts(article);
		return ArticleStatsDto.from(likeCount, commentCount, repostCount);
	}

	//JWT 토큰 기반 사용자 정보 반환 메소드
	private User getUser(HttpServletRequest request) {

		return jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(jwtService::extractEmail)
			.flatMap(userRepository::findByEmail)
			.orElseThrow(UserNotFoundException::new);
	}
}
