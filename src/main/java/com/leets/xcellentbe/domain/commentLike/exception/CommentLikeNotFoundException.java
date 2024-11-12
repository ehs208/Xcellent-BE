package com.leets.xcellentbe.domain.commentLike.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class CommentLikeNotFoundException extends CommonException {
	public CommentLikeNotFoundException() {
		super(ErrorCode.COMMENT_LIKE_NOT_FOUND);
	}
}
