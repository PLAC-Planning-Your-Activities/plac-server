package com.plac.common;

public interface Constant {

    String USER_ROLE = "USER";

    String[] ALL_PERMIT_PATHS = new String[]{
            "/api/users/one",
            "/api/login",
            "/api/users/emails/availability",
            "/api/login/**",
            "/api/social-login",
            "/api/social-login/**",
            "/api/emails/send-verification-email",
            "/api/emails/verify-code",
    };

    String[] USER_ROLE_PERMIT_PATHS = new String[]{
            "/api/**",
            "/api/test/**",
    };

    String[] PLACE_REVIEW_TAGS = new String[]{
            "혼자", "친구", "연인", "아이", "부모님", "반려동물",
            "데이트", "SNS 핫플레이스", "힐링", "먹방", "가성비", "분위기", "혼자놀기"
    };
}
