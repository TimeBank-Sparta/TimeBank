// package com.timebank.notification_service.infrastructure.email;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Component;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Component
// @Slf4j
// @RequiredArgsConstructor
// public class EmailNotificationSender {
//
// 	private final JavaMailSender mailSender;
//
// 	@Value("${spring.mail.username:no-reply@yourdomain.com}")
// 	private String senderEmail;
//
// 	public void send(String recipientEmail, String message) {
// 		SimpleMailMessage email = new SimpleMailMessage();
// 		email.setFrom(senderEmail);
// 		email.setTo(recipientEmail);
// 		email.setSubject("타임뱅크 알림");
// 		email.setText(message);
//
// 		try {
// 			mailSender.send(email);
// 			log.info("이메일 발송 완료: {}", recipientEmail);
// 		} catch (Exception e) {
// 			log.error("이메일 발송 실패: {}", e.getMessage());
// 		}
// 	}
// }
