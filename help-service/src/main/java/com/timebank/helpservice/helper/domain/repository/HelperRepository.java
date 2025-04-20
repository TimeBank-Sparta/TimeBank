package com.timebank.helpservice.helper.domain.repository;

import java.util.List;
import java.util.Optional;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

public interface HelperRepository {

	Helper save(Helper helper);

	Optional<Helper> findById(Long id);

	List<Helper> findByHelpRequestId(Long helpRequestId);

	void deleteHelperStatusSupported(Long helpRequestId);

	boolean existsByHelpRequestIdAndUserId(Long helpRequestId, Long id);

	long countByHelpRequestIdAndApplicantStatus(Long id, ApplicantStatus applicantStatus);
}
