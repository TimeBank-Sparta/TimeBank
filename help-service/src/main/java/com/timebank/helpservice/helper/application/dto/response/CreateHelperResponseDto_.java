// package com.timebank.helpservice.helper.application.dto.response;
//
// import java.util.List;
// import java.util.stream.Collectors;
//
//
// import lombok.Builder;
//
// @Builder
// public record CreateHelperResponseDto_(
// 	Long helpRequestId,
// 	String title,
// 	List<HelperResponse> helperResponseDtoList
// ) {
// 	public static CreateHelperResponseDto_ from(HelpRequest helpRequest) {
// 		return CreateHelperResponseDto_.builder()
// 			.helpRequestId(helpRequest.getId())
// 			.title(helpRequest.getTitle())
// 			.helperResponseDtoList(helpRequest.getHelperSet().stream()
// 				.map(HelperResponse::from)
// 				.collect(Collectors.toList()))
// 			.build();
// 	}
// }
