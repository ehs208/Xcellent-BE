package com.leets.xcellentbe.domain.articleMedia.domain;

import java.util.UUID;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@NotNull
	@Column(columnDefinition = "VARCHAR(30)")
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@Builder
	private ArticleMedia(Article article, String filePath, DeletedStatus deletedStatus) {
		this.article = article;
		this.filePath = filePath;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public static ArticleMedia createArticleMedia (Article article, String filePath) {
		return ArticleMedia.builder()
			.article(article)
			.filePath(filePath)
			.deletedStatus(DeletedStatus.NOT_DELETED)
			.build();
	}

	public void deleteMedia() {
		this.deletedStatus = DeletedStatus.DELETED;
	}
}
