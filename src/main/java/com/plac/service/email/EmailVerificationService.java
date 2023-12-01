package com.plac.service.email;

import com.plac.domain.EmailVerification;
import com.plac.domain.mappedenum.EmailVerificationContentType;
import com.plac.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;

    @Value("${plac.email.expire-minutes.signup}")
    private final long SIGN_UP_EMAIL_EXP_MINUTES = 3L;

    public void sendSignupVerificationEmail(String email) {
        EmailVerification emailVerification = EmailVerification.createForSignup(email, SIGN_UP_EMAIL_EXP_MINUTES);
        emailVerificationRepository.save(emailVerification);
        emailService.sendSimpleMessage(
                emailVerification.getReceiptEmail(),
                "[PLAC] 가입 인증 번호 안내",
                "<html>\n" +
                        "<body>\n" +
                        "    <h3>안녕하세요. 인증 번호 확인 후 이메일 인증을 완료해 주세요.</h3>\n" +
                        "    <p>인증 번호 : " + emailVerification.getContent() + "</p>\n" +
                        "</body>" +
                        "</html>"
        );
    }

    public void verifySignupEmail(String email, int verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailAndContentTypeOrderByCreatedAtDesc(email, EmailVerificationContentType.SIGNUP)
                .orElseThrow(() -> new RuntimeException("인증 번호가 없습니다. 재전송하세요."));

        emailVerification.checkAndValidate();
        emailVerification.matchVerificationCode(verificationCode);
    }
}
