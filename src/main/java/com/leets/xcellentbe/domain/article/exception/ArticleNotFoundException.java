package com.leets.xcellentbe.domain.article.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ArticleNotFoundException extends CommonException {
	public ArticleNotFoundException() {

		super(ErrorCode.ARTICLE_NOT_FOUND);
	}
}
