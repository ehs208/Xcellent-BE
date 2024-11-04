package com.leets.xcellentbe.domain.article.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleRequestDto {
	private UUID articleId;
	private String content;
	private Boolean isPinned;
	private DeletedStatus deletedStatus;
	private Long writerId;
	private List<String> hashtags;
	private UUID rePostId;
	private List<MultipartFile> newMediaFiles;
	private List<UUID> deleteMediaIds;
}
