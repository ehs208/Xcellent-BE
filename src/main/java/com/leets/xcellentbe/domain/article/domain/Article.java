package com.leets.xcellentbe.domain.article.domain;

import java.util.List;
import java.util.UUID;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID postId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@NotNull
	@Column
	private String content;

	@NotNull
	@Column
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@NotNull
	@Column
	private Boolean isPinned;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repost_id")
	private Article reArticle;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag_id")
	private List<Hashtag> hashtags;

	public Article(User writer, String content, DeletedStatus deletedStatus, Boolean isPinned, Article reArticle,
		List<Hashtag> hashtags) {
		this.writer = writer;
		this.content = content;
		this.deletedStatus = deletedStatus;
		this.isPinned = isPinned;
		this.reArticle = reArticle;
		this.hashtags = hashtags;
	}
}
