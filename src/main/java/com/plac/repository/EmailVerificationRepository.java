package com.plac.repository;

import com.plac.domain.EmailVerification;
import com.plac.mappedenum.EmailVerificationContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByReceiptEmailAndContentTypeOrderByCreatedAtDesc(String receiptEmail, EmailVerificationContentType contentType);
}
