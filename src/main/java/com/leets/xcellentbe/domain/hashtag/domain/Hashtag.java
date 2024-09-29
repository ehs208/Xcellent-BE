package com.leets.xcellentbe.domain.hashtag.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

	@Id
	@Column(name = "hashtag_id")
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID HashtagId;

	@NotNull
	@Column(length = 30)
	private String content;
}
