package com.plac.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class AbstractBaseDocument {
    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
