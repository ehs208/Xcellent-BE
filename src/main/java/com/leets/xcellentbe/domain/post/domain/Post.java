package com.leets.xcellentbe.domain.post.domain;

import java.util.List;
import java.util.UUID;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID PostId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@NotNull
	@Column
	private String content;

	@NotNull
	@Column
	private DeletedStatus deletedStatus;

	@NotNull
	@Column
	private Boolean isPinned;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repost_id")
	private Post rePost;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag_id")
	private List<Hashtag> hashtags;

	public Post(User writer, String content, DeletedStatus deletedStatus, Boolean isPinned, Post rePost,
		List<Hashtag> hashtags) {
		this.writer = writer;
		this.content = content;
		this.deletedStatus = deletedStatus;
		this.isPinned = isPinned;
		this.rePost = rePost;
		this.hashtags = hashtags;
	}
}
