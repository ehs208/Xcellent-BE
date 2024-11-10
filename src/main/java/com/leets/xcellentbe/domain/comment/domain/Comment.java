package com.leets.xcellentbe.domain.comment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leets.xcellentbe.domain.article.domain.Article;
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
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID commentId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@Column(length = 50)
	private String content;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	@NotNull
	@Column(columnDefinition = "VARCHAR(30)")
	@Enumerated(EnumType.STRING)
	private DeletedStatus deletedStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment;

	@OneToMany(mappedBy = "parentComment")
	private List<Comment> comments;

	private int viewCnt, likeCnt, commentCnt;

	@Builder
	private Comment(User writer, String content, Article article) {
		this.writer = writer;
		this.content = content;
		this.article = article;
		this.deletedStatus = DeletedStatus.NOT_DELETED;
	}

	public static Comment createComment(User writer, String content, Article article) {
		return Comment.builder()
			.writer(writer)
			.content(content)
			.article(article)
			.build();
	}
	public void updateParentComment(Comment parentComment) {
		this.parentComment = parentComment;
	}

	public void addComment(List<Comment> comments) {
		if(this.comments == null){
			this.comments = new ArrayList<>();
		}
		this.comments.addAll(comments);
	}

	public void deleteComment() {
		this.deletedStatus = DeletedStatus.DELETED;
	}

	public void  updateViewCount() {
		this.viewCnt++;
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
