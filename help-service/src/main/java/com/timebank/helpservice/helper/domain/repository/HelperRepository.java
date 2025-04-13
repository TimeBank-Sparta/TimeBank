package com.timebank.helpservice.helper.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

public interface HelperRepository {

	Helper save(Helper helper);

	Optional<Helper> findById(Long id);

	Optional<Page<Helper>> findByHelpRequestId(Long helpRequestId, Pageable pageable);

	void deleteHelperStatusSupported(Long helpRequestId);

	boolean existsByUserId(Long id);

	long countByHelpRequestIdAndApplicantStatus(Long id, ApplicantStatus applicantStatus);
}
