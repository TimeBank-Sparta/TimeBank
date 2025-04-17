package com.timebank.helpservice.help_request.application.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEvent;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.helpservice.help_request.application.dto.request.CreateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.request.HelpRequestToHelperKafkaDto;
import com.timebank.helpservice.help_request.application.dto.request.HoldPointRequestDto;
import com.timebank.helpservice.help_request.application.dto.request.SearchHelpRequestQuery;
import com.timebank.helpservice.help_request.application.dto.request.UpdateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.response.CreateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.UpdateHelpRequestResponse;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HelpRequestService {

	private final HelpRequestRepository helpRequestRepository;
	private final HelpRequestEventProducer eventProducer;
	private final PointClient pointClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * 도움 요청 글 생성 (작성 시 알림 이벤트 발행 – 필요에 따라 추가)
	 */
	@Transactional
	public CreateHelpRequestResponse createHelpRequest(CreateHelpRequestCommand command, Long userId) {
		HelpRequest helpRequest = HelpRequest.createFrom(command.toHelpRequestInfoWithUserID(userId));
		helpRequest = helpRequestRepository.save(helpRequest);

		// (선택 사항) 도움 요청 글 생성 시 알림 이벤트 (예: POST_CREATED)
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpRequest.getRequesterId())
			.type(NotificationType.POST_CREATED)
			.message("도움 요청 글이 생성되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)
			.build();
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);

		pointClient.holdPoint(HoldPointRequestDto.of(userId, command.requestedPoint()));
		return CreateHelpRequestResponse.from(helpRequest);
	}

	/**
	 * 도움 요청 글 단건 조회
	 */
	@Transactional(readOnly = true)
	public HelpRequestResponse findById(Long helpRequestId) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);
		return HelpRequestResponse.from(helpRequest);
	}

	/**
	 * 도움 요청 글 검색 (페이지네이션)
	 */
	@Transactional(readOnly = true)
	public Page<HelpRequestResponse> searchHelpRequest(SearchHelpRequestQuery request, Pageable pageable) {
		return helpRequestRepository.search(request.toHelpRequestQuery(), pageable)
			.map(HelpRequestResponse::from);
	}

	/**
	 * 도움 요청 글 수정
	 */
	@Transactional
	public UpdateHelpRequestResponse updateHelpRequest(UpdateHelpRequestCommand command, Long helpRequestId) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);
		helpRequest.update(command.toHelpRequestInfo());

		// (선택 사항) 업데이트 알림 발행 – 필요 시
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpRequest.getRequesterId())
			.type(NotificationType.USER_INFO_UPDATE)
			.message("도움 요청 글이 수정되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);

		return UpdateHelpRequestResponse.from(helpRequest);
	}

	/**
	 * 도움 요청 글 종료(모집 완료) 시 도움 거래 시작 전 알림 발행
	 */
	@Transactional
	public UpdateHelpRequestResponse completeHelpRequest(Long helpRequestId, Long userId) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);

		if (!Objects.equals(helpRequest.getRequesterId(), userId)) {
			throw new IllegalArgumentException("작성자만 모집완료 가능합니다");
		}

		eventProducer.sendToHelper(HelpRequestToHelperKafkaDto.of(helpRequestId));
		// 모집완료 처리
		helpRequest.completePostStatus();

		// 도움 요청 글 모집 완료 알림 발행:
		// 예시 - 도움 요청 글 작성자에게 "모집이 완료되었습니다. 지원자 선별 후 거래가 진행됩니다." 알림 전송
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpRequest.getRequesterId())
			.type(NotificationType.POST_RECRUITMENT_COMPLETED)
			.message("도움 요청 글의 모집이 완료되었습니다. 지원자 선별 후 거래가 시작됩니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);

		return UpdateHelpRequestResponse.from(helpRequest);
	}

	/**
	 * 도움 요청 글 삭제 (알림 이벤트 발행 – 선택 사항)
	 */
	public void deleteHelpRequest(Long helpRequestId, String username) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);
		helpRequest.delete(username);

		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpRequest.getRequesterId())
			.type(NotificationType.POST_CREATED) // 필요 시 별도 DELETED 타입으로 정의 가능
			.message("도움 요청 글이 삭제되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.DELETED)
			.build();
		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
	}

	public boolean existHelpRequest(Long helpRequestId) {
		return helpRequestRepository.existsById(helpRequestId);
	}

	private HelpRequest getHelpRequestOrThrow(Long helpRequestId) {
		return helpRequestRepository.findById(helpRequestId)
			.orElseThrow(() -> new CustomNotFoundException("게시글이 없습니다."));
	}

}
