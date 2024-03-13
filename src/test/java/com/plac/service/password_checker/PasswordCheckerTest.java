//package com.plac.service.password_checker;
//
//import com.plac.domain.user.service.PasswordChecker;
//import com.plac.exception.custom.WeakPasswordException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class PasswordCheckerTest {
//
//    private PasswordChecker passwordChecker;
//
//    @BeforeEach
//    void setUp(){
//        passwordChecker = new PasswordChecker();
//    }
//
//    @Test
//    @DisplayName("유효한 비밀번호는 예외를 발생시키지 않아야 함")
//    void checkValidPassword() {
//        assertDoesNotThrow(() -> passwordChecker.checkWeakPassword("ValidPass123"));
//    }
//
//    @Test
//    @DisplayName("너무 짧은 비밀번호는 WeakPasswordException을 발생시켜야 함")
//    void checkShortPassword() {
//        assertThrows(WeakPasswordException.class, () -> passwordChecker.checkWeakPassword("123"));
//    }
//
//    @Test
//    @DisplayName("너무 긴 비밀번호는 WeakPasswordException을 발생시켜야 함")
//    void checkLongPassword() {
//        assertThrows(WeakPasswordException.class, () -> passwordChecker.checkWeakPassword("ThisPasswordIsWayTooLong12345"));
//    }
//}