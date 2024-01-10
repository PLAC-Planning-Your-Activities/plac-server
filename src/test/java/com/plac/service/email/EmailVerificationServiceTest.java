package com.plac.service.email;

import com.plac.domain.EmailVerification;
import com.plac.domain.user.entity.User;
import com.plac.repository.EmailVerificationRepository;
import com.plac.domain.user.repository.UserRepository;
import com.plac.util.RandomGeneratorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailVerificationServiceTest {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @MockBean
    private RandomGeneratorUtil randomGeneratorUtil;

    public EmailVerificationServiceTest() {
    }

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .username("ejcong99@naver.com")
                .password("password")
                .provider("normal")
                .roles("ROLE_USER")
                .build();
        userRepository.save(user); // userId = 1L;
    }

    @Test
    public void sendSignupVerificationEmailTest() {
        assertDoesNotThrow(() -> emailVerificationService.sendSignupVerificationEmail("ejcong99@naver.com"));
    }

    @Test
    public void checkSignupEmailTest() {
        String receiptEmail = "ejcong99@naver.com";
        long expMinutes = 3L;

        EmailVerification emailVerification1 = EmailVerification.createForSignup(receiptEmail, expMinutes);
        EmailVerification emailVerification2 = EmailVerification.createForSignup(receiptEmail, expMinutes);
        EmailVerification emailVerification3 = EmailVerification.createForSignup(receiptEmail, expMinutes);

        int verificationCode = Integer.valueOf(emailVerification3.getContent());

        emailVerificationRepository.save(emailVerification1);
        emailVerificationRepository.save(emailVerification2);
        emailVerificationRepository.save(emailVerification3);

        assertDoesNotThrow(() -> emailVerificationService.verifySignupEmail(receiptEmail, Integer.valueOf(verificationCode)));
    }
}
