package com.dmdev.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateSubscriptionDtoTest {

    @Test
    void createDtoSuccess() {
        Instant now = Instant.now();
        var dto = getDto(now);

        assertAll(
                () -> assertThat(dto.getUserId()).isEqualTo(1),
                () -> assertThat(dto.getName()).isEqualTo("dummy"),
                () -> assertThat(dto.getProvider()).isEqualTo("dummy"),
                () -> assertThat(dto.getExpirationDate()).isEqualTo(now)
        );
    }

    private CreateSubscriptionDto getDto(Instant time) {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("dummy")
                .provider("dummy")
                .expirationDate(time)
                .build();
    }
}
