package com.timebank.helpservice.helper.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.helper.domain.model.Helper;
import com.timebank.helpservice.helper.domain.repository.HelperRepository;

public interface JpaHelperRepository extends HelperRepository, JpaRepository<Helper, Long> {
}
