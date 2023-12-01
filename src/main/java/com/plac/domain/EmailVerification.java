package com.plac.domain;

import com.plac.domain.mappedenum.EmailVerificationContentType;
import com.plac.util.RandomGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "email_verification")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiptEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private EmailVerificationContentType contentType;

    private String content;

    private boolean checkedStatus;

    private LocalDateTime expireAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Long userId;

    public static EmailVerification createForSignup(String email, long expMinutes) {
        String content = String.valueOf(RandomGeneratorUtil.generateRandomSixDigitNumber());
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(expMinutes);

        return EmailVerification.builder()
                .receiptEmail(email)
                .contentType(EmailVerificationContentType.SIGNUP)
                .content(content)
                .checkedStatus(false)
                .expireAt(expireAt)
                .build();
    }

    public void checkAndValidate() {
        validateCanCheck();
        check();
    }

    private void check() {
        this.checkedStatus = true;
    }

    private void validateCanCheck() {
        LocalDateTime current = LocalDateTime.now();

        if (checkedStatus) {
            throw new IllegalStateException("이미 인증된 번호입니다.");
        }
        if (expireAt.isBefore(current)) {
            throw new IllegalStateException("인증 번호가 만료되었습니다.");
        }
    }

    public void matchVerificationCode(int code) {
        int contentCode = Integer.valueOf(content);
        if (contentCode != code) {
            throw new RuntimeException("인증 번호가 일치하지 않습니다.");
        }
    }
}
