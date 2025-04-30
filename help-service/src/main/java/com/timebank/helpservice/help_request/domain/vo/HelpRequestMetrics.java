package com.timebank.helpservice.help_request.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public class HelpRequestMetrics {
	private int viewCount;

	private int helperCount;
}
