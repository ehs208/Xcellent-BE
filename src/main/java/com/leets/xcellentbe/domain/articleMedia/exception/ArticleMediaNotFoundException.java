package com.leets.xcellentbe.domain.articleMedia.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ArticleMediaNotFoundException extends CommonException {
	public ArticleMediaNotFoundException() {

		super(ErrorCode.ARTICLE_MEDIA_NOT_FOUND);
	}
}
