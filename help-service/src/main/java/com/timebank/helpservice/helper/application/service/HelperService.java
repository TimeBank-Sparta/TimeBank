package com.timebank.helpservice.helper.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.helper.application.client.HelpRequestClient;
import com.timebank.helpservice.helper.application.dto.request.CreateHelperCommand;
import com.timebank.helpservice.helper.application.dto.request.HelpRequestFeignDto;
import com.timebank.helpservice.helper.application.dto.request.HelperToTradingKafkaDto;
import com.timebank.helpservice.helper.application.dto.response.CreateHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FindHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FromHelpRequestKafkaDto;
import com.timebank.helpservice.helper.application.dto.response.UpdateHelperResponse;
import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;
import com.timebank.helpservice.helper.domain.repository.HelperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelperService {
	private final HelperRepository helperRepository;
	private final HelpRequestClient helpRequestClient;
	private final HelperEventProducer helperEventProducer;

	@Transactional
	public CreateHelperResponse createHelper(CreateHelperCommand command) {
		if (helperRepository.existsByUserId(command.userId())) {
			throw new CustomNotFoundException("중복된 지원자 입니다.");
		}
		return CreateHelperResponse.from(helperRepository.save(Helper.createFrom(command.toHelperInfo())));
	}

	@Transactional
	public UpdateHelperResponse acceptHelper(Long helperId) {
		Helper helper = helperRepository.findById(helperId).orElseThrow(() ->
			new CustomNotFoundException("지원자가 없습니다."));

		HelpRequestFeignDto helpRequest =
			helpRequestClient.getHelpRequestById(helper.getHelpRequestId());

		long acceptedHelperCnt = helperRepository.countByHelpRequestIdAndApplicantStatus(
			helper.getHelpRequestId(), ApplicantStatus.ACCEPTED);

		if (acceptedHelperCnt >= helpRequest.recruitmentCount()) {
			throw new IllegalArgumentException("선별인원 초과");
		}

		helperEventProducer.sendToHelpTrading(HelperToTradingKafkaDto.of(
			helper.getHelpRequestId(),
			helperId,
			helpRequest.requesterId(),
			helpRequest.requestedPoint()));

		helper.acceptHelperStatus();

		return UpdateHelperResponse.from(helper);
	}

	@Transactional(readOnly = true)
	public Page<FindHelperResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		Page<Helper> helpers = helperRepository.findByHelpRequestId(helpRequestId, pageable).orElseThrow(() ->
			new CustomNotFoundException("지원자가 없습니다."));

		//TODO 유저 정보 (리뷰평점, 한줄평 등등)가져온후 리턴 (feign)

		return null;
	}

	@Transactional
	public void deleteHelpersByStatusSupported(FromHelpRequestKafkaDto dto) {
		helperRepository.deleteHelperStatusSupported(dto.helpRequestId());
	}
}
