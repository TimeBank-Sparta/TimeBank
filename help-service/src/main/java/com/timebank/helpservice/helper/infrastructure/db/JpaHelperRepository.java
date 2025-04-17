package com.timebank.helpservice.helper.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

public interface JpaHelperRepository extends JpaRepository<Helper, Long> {
	long countByHelpRequestIdAndApplicantStatus(Long id, ApplicantStatus applicantStatus);
}
