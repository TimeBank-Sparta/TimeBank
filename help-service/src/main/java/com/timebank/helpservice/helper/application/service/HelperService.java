package com.timebank.helpservice.helper.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.helper.application.dto.request.CreateHelperCommand;
import com.timebank.helpservice.helper.application.dto.response.CreateHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FindHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.UpdateHelperResponse;
import com.timebank.helpservice.helper.domain.model.Helper;
import com.timebank.helpservice.helper.domain.repository.HelperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelperService {
	private final HelperRepository helperRepository;

	@Transactional
	public CreateHelperResponse createHelper(CreateHelperCommand command) {
		//TODO 게시글 존재 여부 확인
		return CreateHelperResponse.from(helperRepository.save(Helper.createFrom(command.toHelperInfo())));
	}

	@Transactional
	public UpdateHelperResponse acceptHelper(Long helperId) {
		Helper helper = helperRepository.findById(helperId).orElseThrow(() ->
			new CustomNotFoundException("지원자가 없습니다."));

		helper.acceptHelperStatus();

		//TODO 거래내역이 생성되어야함

		return UpdateHelperResponse.from(helper);
	}

	@Transactional(readOnly = true)
	public Page<FindHelperResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		Page<Helper> helpers = helperRepository.findByHelpRequestId(helpRequestId, pageable).orElseThrow(() ->
			new CustomNotFoundException("지원자가 없습니다."));

		//TODO 유저 정보 (리뷰평점, 한줄평 등등)가져온후 리턴

		return null;
	}
}
