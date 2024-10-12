package com.leets.xcellentbe.global.auth.login.oauth;

import java.security.SecureRandom;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
	private static final int PASSWORD_LENGTH = 12; // 원하는 비밀번호 길이

	private final PasswordEncoder passwordEncoder;

	public PasswordUtil() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	// 랜덤 비밀번호 생성 및 해싱하여 반환
	public String generateHashedRandomPassword() {
		String randomPassword = generateRandomPassword();
		return hashPassword(randomPassword);
	}

	// 랜덤 비밀번호 생성
	private String generateRandomPassword() {
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			password.append(CHARACTERS.charAt(index));
		}

		return password.toString();
	}

	// 비밀번호 해싱
	private String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public static void main(String[] args) {
		PasswordUtil passwordUtil = new PasswordUtil();

		// 랜덤 비밀번호 생성 및 해싱
		String hashedPassword = passwordUtil.generateHashedRandomPassword();
		System.out.println("해싱된 랜덤 비밀번호: " + hashedPassword);
	}
}
