package com.timebank.helpservice.helper.application.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.helper.application.client.HelpRequestClient;
import com.timebank.helpservice.helper.application.client.UserClient;
import com.timebank.helpservice.helper.application.dto.request.CreateHelperCommand;
import com.timebank.helpservice.helper.application.dto.request.GetUserInfoFeignRequest;
import com.timebank.helpservice.helper.application.dto.request.HelpRequestFeignDto;
import com.timebank.helpservice.helper.application.dto.request.HelperToTradingKafkaDto;
import com.timebank.helpservice.helper.application.dto.response.CreateHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FindHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FromHelpRequestKafkaDto;
import com.timebank.helpservice.helper.application.dto.response.GetUserInfoFeignResponse;
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
	private final UserClient userClient;

	@Transactional
	public CreateHelperResponse createHelper(CreateHelperCommand command, Long userId) {
		if (helperRepository.existsByUserId(userId)) {
			throw new CustomNotFoundException("중복된 지원자 입니다.");
		}
		if (Objects.equals(command.requesterId(), userId)) {
			throw new CustomNotFoundException("작성자는 지원 불가능합니다.");
		}
		return CreateHelperResponse.from(helperRepository.save(Helper.createFrom(
			command.toHelperInfoWithUserId(userId))));
	}

	@Transactional
	public void acceptHelper(Long helperId) {
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
			helper.getUserId(),
			helpRequest.requesterId(),
			helpRequest.requestedPoint()));

		helper.acceptHelperStatus();
	}

	@Transactional(readOnly = true)
	public Page<FindHelperResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		List<Helper> helpers = helperRepository.findByHelpRequestId(helpRequestId);

		List<GetUserInfoFeignResponse> userInfoList = userClient.getUserInfoByHelper(helpers.stream()
			.map(GetUserInfoFeignRequest::from)
			.toList());

		Map<Long, GetUserInfoFeignResponse> userInfoMap = userInfoList.stream()
			.collect(Collectors.toMap(GetUserInfoFeignResponse::userId, Function.identity()));

		List<FindHelperResponse> content = helpers.stream()
			.map(helper -> {
				GetUserInfoFeignResponse userInfo = userInfoMap.get(helper.getUserId());
				return FindHelperResponse.of(userInfo, helper);
			})
			.toList();

		return new PageImpl<>(content, pageable, helpers.size());
	}

	@Transactional

	public void deleteHelpersByStatusSupported(FromHelpRequestKafkaDto dto) {
		helperRepository.deleteHelperStatusSupported(dto.helpRequestId());
	}
}
