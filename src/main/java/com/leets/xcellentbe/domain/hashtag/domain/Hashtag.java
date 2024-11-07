package com.leets.xcellentbe.domain.hashtag.domain;

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
public class Hashtag extends BaseTimeEntity {

	@Id
	@Column(name = "hashtag_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID HashtagId;

	@NotNull
	@Column(length = 30)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	@NotNull
	@Column(columnDefinition = "VARCHAR(30)")
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@Builder
	private Hashtag(Article article, String content, DeletedStatus deletedStatus) {
		this.article = article;
		this.content = content;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public static Hashtag create(Article article, String content) {
		return Hashtag.builder()
			.article(article)
			.content(content)
			.build();
	}

	public void deleteHashtag() {
		this.deletedStatus = DeletedStatus.DELETED;
	}
}
