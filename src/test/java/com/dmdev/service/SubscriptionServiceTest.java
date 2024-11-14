package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.exception.SubscriptionException;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.CreateSubscriptionValidatorTest;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private CreateSubscriptionMapper createSubscriptionMapper;
    @Mock
    private CreateSubscriptionValidator createSubscriptionValidator;
    @Mock
    private Clock clock;
    @InjectMocks
    private SubscriptionService service;

    @Test
    void upsertSuccessSubscriptionIsExists() {
        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        Subscription subscription = getSubscription();
        List<Subscription> subscriptions = List.of(subscription);

        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(dto);
        doReturn(subscriptions).when(subscriptionDao).findByUserId(dto.getUserId());
        doReturn(subscription).when(subscriptionDao).upsert(subscription);

        Subscription actualResult = service.upsert(dto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(subscription);

        verify(subscriptionDao).findByUserId(dto.getUserId());
        verify(subscriptionDao).upsert(subscription);
    }

    @Test
    void upsertSuccessSubscriptionDoesNotExists() {
        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        Subscription subscription = getSubscription();
        List<Subscription> subscriptions = List.of();

        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(dto);
        doReturn(subscriptions).when(subscriptionDao).findByUserId(dto.getUserId());
        doReturn(subscription).when(createSubscriptionMapper).map(dto);
        doReturn(subscription).when(subscriptionDao).upsert(subscription);

        Subscription actualResult = service.upsert(dto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(subscription);

        verify(subscriptionDao).findByUserId(dto.getUserId());
        verify(createSubscriptionMapper).map(dto);
        verify(subscriptionDao).upsert(subscription);
    }

    @Test
    void shouldUpsertThrowsException() {
        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(101, "test exception"));

        doReturn(validationResult).when(createSubscriptionValidator).validate(dto);

        assertThrows(ValidationException.class, () -> service.upsert(dto));
    }


    @Test
    void shouldCanselThrowsIllegalArgumentExceptionIfSubscriptionDoesNotExists() {
        Subscription subscription = getSubscription();
        doReturn(Optional.empty()).when(subscriptionDao).findById(subscription.getId());

        assertThrows(IllegalArgumentException.class, () -> service.cancel(subscription.getId()));

        verify(subscriptionDao).findById(subscription.getId());
    }

    @Test
    void shouldCanselThrowsSubscriptionExceptionIfStatusDoesNotActive() {
        Subscription subscription = getSubscription();
        subscription.setStatus(Status.CANCELED);

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());

        SubscriptionException e = assertThrows(SubscriptionException.class, () -> service.cancel(subscription.getId()));

        assertEquals(e.getMessage(), String.format("Only active subscription %d can be canceled", subscription.getId()));
    }

    @Test
    void canselSuccess() {
        Subscription subscription = getSubscription();

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        doReturn(subscription).when(subscriptionDao).update(subscription);

        service.cancel(subscription.getId());

        verify(subscriptionDao).update(subscription);
    }

    @Test
    void shouldExpireThrowsIllegalArgumentExceptionIfSubscriptionDoesNotExists() {
        Subscription subscription = getSubscription();
        doReturn(Optional.empty()).when(subscriptionDao).findById(subscription.getId());

        assertThrows(IllegalArgumentException.class, () -> service.expire(subscription.getId()));

        verify(subscriptionDao).findById(subscription.getId());
    }

    @Test
    void shouldExpireThrowsSubscriptionExceptionIfStatusDoesNotActive() {
        Subscription subscription = getSubscription();
        subscription.setStatus(Status.EXPIRED);

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());

        SubscriptionException e = assertThrows(SubscriptionException.class, () -> service.expire(subscription.getId()));

        assertEquals(e.getMessage(), String.format("Subscription %d has already expired", subscription.getId()));
    }

    @Test
    void expireSuccess() {
        Subscription subscription = getSubscription();
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        doReturn(subscription).when(subscriptionDao).update(subscription);

        service.expire(subscription.getId());

        verify(subscriptionDao).update(subscription);
    }

    private CreateSubscriptionDto getCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .expirationDate(Instant.now().plusSeconds(10000))
                .provider("apple")
                .build();
    }

    private Subscription getSubscription() {
        return Subscription.builder()
                .id(1)
                .status(Status.ACTIVE)
                .expirationDate(Instant.now().plusSeconds(10000))
                .provider(Provider.APPLE)
                .name("Ivan")
                .userId(1)
                .build();
    }
}
