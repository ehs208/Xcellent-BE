package com.leets.xcellentbe.domain.user.domain;

import org.hibernate.grammars.hql.HqlParser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class BirthDay {

	@NotNull
	@Column(length=4)
	private int year;

	@NotNull
	@Column(length=2)
	private int month;

	@NotNull
	@Column(length=2)
	private int day;

	@Builder
	private BirthDay(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
}
