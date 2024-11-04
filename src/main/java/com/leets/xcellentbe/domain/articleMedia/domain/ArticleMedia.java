package com.leets.xcellentbe.domain.articleMedia.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.shared.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleMedia extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ArticleMediaId;

	@NotNull
	@Column
	private String filePath;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	private ArticleMedia(Article article, String filePath) {
		this.article = article;
		this.filePath = filePath;
	}

	public static ArticleMedia createArticleMedia (Article article, String filePath) {
		return ArticleMedia.builder()
			.article(article)
			.filePath(filePath)
			.build();
	}

	public void updateArticleMedia(String filePath) {
		this.filePath = filePath;
	}
}
