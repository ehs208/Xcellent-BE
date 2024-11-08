package com.leets.xcellentbe.domain.hashtag.HashtagService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.hashtag.domain.repository.HashtagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HashtagService {

	private final HashtagRepository hashtagRepository;

	//해시태그 추출 & 저장
	public List<String> extractHashtagNames(String content){
		List<String> hashtagNames = new ArrayList<>();
		Pattern hashtagPattern = Pattern.compile("(?<=#)(\\w+)");
		Matcher matcher = hashtagPattern.matcher(content);

		while (matcher.find()){
			String hashtagName = matcher.group(1);
			hashtagNames.add(hashtagName);
		}
		return hashtagNames;
	}

	public List<Hashtag> createHashtags(Article article, List<String> hashtagNames) {
		List<Hashtag> hashtags = new ArrayList<>();
		for(String hashtagName : hashtagNames){
			Hashtag hashtag = Hashtag.create(article, hashtagName);
			hashtags.add(hashtag);
		}
		return hashtagRepository.saveAll(hashtags);
	}

	public List<Hashtag> extractAndSaveHashtags(Article article, String content) {
		List<String> hashtagNames = extractHashtagNames(content);  // 해시태그 이름 추출
		return createHashtags(article, hashtagNames);  // 조회 및 생성
	}

	//해시태그 삭제
	public void deleteHashtags(Article article) {
		List<Hashtag> hashtagList = hashtagRepository.findByArticle_ArticleId(article.getArticleId());

		if (!(hashtagList.isEmpty())) {
			for (Hashtag hashtag : hashtagList) {
				hashtag.deleteHashtag();
			}
		}
	}
}
