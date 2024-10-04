package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ArticleNotFoundException extends CommonException {
	public ArticleNotFoundException() {

		super(ErrorCode.ARTICLE_NOT_FOUND);
	}
}
