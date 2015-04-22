package se.fearless.spacedweb.services.mail;

import org.springframework.mail.SimpleMailMessage;

public class ResetPasswordMail extends SimpleMailMessage {
	public ResetPasswordMail(String email, String token, String baseUrl) {
		this.setFrom("support@fearlessgames.se");
		this.setSubject("Reset password request from fearless games");
		StringBuilder sb = new StringBuilder("You or someone has requested a password request for your account at fearless games. Please ignore this request if it wasn't you. Otherwise follow the link below to reset your password").append("\n");
		sb.append(baseUrl).append(token);
		this.setText(sb.toString());
		this.setTo(email);
	}
}
