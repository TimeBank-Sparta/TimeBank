package com.timebank.notification_service.application.dto;

import lombok.Getter;

@Getter
public class SlackUserResponse {
	private boolean ok;
	private SlackUser user;

	@Getter
	public static class SlackUser {
		private String id;
		private String teamId;
		private String name;
		private String realName;
		private SlackProfile profile;

		@Getter
		public static class SlackProfile {
			private String email;
		}
	}
}
