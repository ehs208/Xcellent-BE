package com.leets.xcellentbe.domain.articleMedia.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleMediaRequestDto {
	private String filePath;
	private UUID articleId;
	private List<MultipartFile> mediaFiles;
}
