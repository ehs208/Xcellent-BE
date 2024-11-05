package com.leets.xcellentbe.domain.articleMedia.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.articleMedia.domain.repository.ArticleMediaRepository;
import com.leets.xcellentbe.domain.articleMedia.exception.ArticleMediaNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleMediaService {

	private final ArticleMediaRepository articleMediaRepository;
	private final S3UploadMediaService s3UploadMediaService;

	//미디어 생성
	public List<ArticleMedia> saveArticleMedia(List<MultipartFile> mediaFiles, Article article) {
		List<ArticleMedia> articleMediaList = new ArrayList<>();

		for(MultipartFile multipartFile : mediaFiles) {
			String fileUrl = s3UploadMediaService.upload(multipartFile,"article");
			ArticleMedia media = ArticleMedia.createArticleMedia(article, fileUrl);
			articleMediaRepository.save(media);
			articleMediaList.add(media);
		}
		return articleMediaList;
	}

	//미디어 조회
	public List<ArticleMedia> getArticleMedia(Article article) {
		List<ArticleMedia> mediaList = articleMediaRepository.findByArticleId(article.getArticleId());

		if (mediaList.isEmpty()) {
			throw new ArticleMediaNotFoundException();
		}

		return mediaList;
	}

	//미디어 삭제
	public void deleteMediaByArticle(Article article) {
		List<ArticleMedia> mediaList = articleMediaRepository.findByArticleId(article.getArticleId());

		if (mediaList.isEmpty()) {
			throw new ArticleMediaNotFoundException();
		}

		for (ArticleMedia media : mediaList) {
			s3UploadMediaService.removeFile(media.getFilePath(), "articles/");
			articleMediaRepository.delete(media);
		}
	}
}
