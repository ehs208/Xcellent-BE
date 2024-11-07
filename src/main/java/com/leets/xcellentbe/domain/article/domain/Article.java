package com.leets.xcellentbe.domain.article.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID articleId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@NotNull
	@Column
	private String content;

	@NotNull
	@Column(columnDefinition = "VARCHAR(30)")
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repost_id")
	private Article rePost;

	@OneToMany(mappedBy = "article")
	private List<Hashtag> hashtags;

	@OneToMany(mappedBy = "article")
	private List<ArticleMedia> mediaList;

	private int viewCnt, repostCnt, likeCnt, commentCnt;


	@Builder
	private Article(User writer, String content, DeletedStatus deletedStatus) {
		this.writer = writer;
		this.content = content;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public static Article createArticle(User writer, String content){
		return Article.builder()
			.writer(writer)
			.content(content)
			.deletedStatus(DeletedStatus.NOT_DELETED)
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

	public void deleteArticle() {
		this.deletedStatus = DeletedStatus.DELETED;
	}

	public void addMedia(List<ArticleMedia> mediaList) {
		if (this.mediaList == null) {
			this.mediaList = new ArrayList<>();
		}
		this.mediaList.addAll(mediaList);
	}

	public void  updateViewCount() {
		this.viewCnt++;
	}

	public void  plusRepostCount() {
		this.repostCnt++;
	}

	public void minusRepostCount() {
		this.repostCnt--;
	}

	public void  plusLikeCount() {
		this.likeCnt++;
	}

	public void minusLikeCount() {
		this.likeCnt--;
	}
	public void  plusCommentCount() {
		this.commentCnt++;
	}

	public void minusCommentCount() {
		this.commentCnt--;
	}
}
