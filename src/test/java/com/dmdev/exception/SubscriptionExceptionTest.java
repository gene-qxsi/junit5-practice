package com.dmdev.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionExceptionTest {

    @Test
    void shouldSubscriptionException() {
        SubscriptionException exception = new SubscriptionException("dummy");
        SubscriptionException exception1 = assertThrows(SubscriptionException.class, () -> {
            throw exception;
        });
        assertEquals(exception.getMessage(), exception1.getMessage());
    }
}
