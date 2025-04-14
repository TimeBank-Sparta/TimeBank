package com.timebank.notification_service.infrastructure.email;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationSender {

	private final JavaMailSender mailSender;
	private final UserRepository userRepository;

	@Value("${spring.mail.username}")
	private String senderEmail;

	public void send(Long userId, String message, Map<String, String> payload) {
		String userEmail = userRepository.findEmailByUserId(userId);

		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(senderEmail);
		email.setTo(userEmail);
		email.setSubject("타임뱅크 알림");

		// 이메일 본문 작성
		StringBuilder emailContent = new StringBuilder();
		emailContent.append(message).append("\n\n");

		if (payload != null) {
			payload.forEach((key, value) -> {
				emailContent.append(key).append(": ").append(value).append("\n");
			});
		}

		email.setText(emailContent.toString());

		try {
			mailSender.send(email);
			log.info("이메일 발송 완료 (userId: {}, email: {})", userId, userEmail);
		} catch (Exception e) {
			log.error("이메일 발송 실패: {}", e.getMessage());
		}
	}
}
