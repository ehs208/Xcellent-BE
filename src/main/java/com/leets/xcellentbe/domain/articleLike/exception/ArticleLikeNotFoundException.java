package com.leets.xcellentbe.domain.articleLike.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ArticleLikeNotFoundException extends CommonException {
	public ArticleLikeNotFoundException() {
		super(ErrorCode.ARTICLE_LIKE_NOT_FOUND);
	}
}
