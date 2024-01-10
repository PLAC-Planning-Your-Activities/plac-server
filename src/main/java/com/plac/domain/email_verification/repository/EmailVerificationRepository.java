package com.plac.domain.email_verification.repository;

import com.plac.domain.email_verification.entity.EmailVerification;
import com.plac.common.mappedenum.EmailVerificationContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByReceiptEmailAndContentTypeOrderByCreatedAtDesc(String receiptEmail, EmailVerificationContentType contentType);
}
