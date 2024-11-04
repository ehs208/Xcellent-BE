package com.leets.xcellentbe.domain.article.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

import jakarta.persistence.CascadeType;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID ArticleId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@NotNull
	@Column
	private String content;

	@NotNull
	@Column(columnDefinition = "VARCHAR(10)")
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@NotNull
	@Column
	private Boolean isPinned;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repost_id")
	private Article rePost;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag_id")
	private List<Hashtag> hashtags;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArticleMedia> mediaList = new ArrayList<>();

	@Builder
	private Article(User writer, String content, DeletedStatus deletedStatus, Boolean isPinned) {
		this.writer = writer;
		this.content = content;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
		this.isPinned = false;
	}

	public static Article createArticle(User writer, String content){
		return Article.builder()
			.writer(writer)
			.content(content)
			.deletedStatus(DeletedStatus.NOT_DELETED)
			.isPinned(false)
			.build();
	}

	public void addRepost(Article rePost) {
		this.rePost = rePost;
	}

	public void addHashtag(List<Hashtag> hashtags) {
		if(this.hashtags == null){
			this.hashtags = new ArrayList<>();
		}
		this.hashtags.addAll(hashtags);
	}

	public void updateArticle(String content) {
		this.content = content;
	}

	public void deleteArticle() {
		this.deletedStatus = DeletedStatus.DELETED;
	}

	public void pinArticle() {
		this.isPinned = true;
	}

	public void unPinArticle() {
		this.isPinned = false;
	}

	public void addMedia(List<ArticleMedia> mediaList) {
		this.mediaList.addAll(mediaList);
	}
}
