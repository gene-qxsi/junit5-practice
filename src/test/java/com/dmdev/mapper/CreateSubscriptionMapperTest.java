package com.dmdev.mapper;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateSubscriptionMapperTest {
    CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void mapSuccess() {
        Instant now = Instant.now();
        CreateSubscriptionDto dto = getDto(now);

        Subscription actualResult = mapper.map(dto);

        assertAll(
                () -> assertThat(dto.getUserId()).isEqualTo(actualResult.getUserId()),
                () -> assertThat(dto.getName()).isEqualTo(actualResult.getName()),
                () -> assertEquals(Provider.findByName(dto.getProvider()), actualResult.getProvider()),
                () -> assertThat(dto.getExpirationDate()).isEqualTo(actualResult.getExpirationDate()),
                () -> assertEquals(actualResult.getStatus(), Status.ACTIVE)
        );
    }

    private CreateSubscriptionDto getDto(Instant time) {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("dummy")
                .provider("apple")
                .expirationDate(time)
                .build();
    }
}
