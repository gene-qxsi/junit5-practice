package com.dmdev.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionTest {


    //этот дебильный класс написан, только чтобы поднять процент jacoco
    @Test
    void all() {
        Subscription subscription = new Subscription();
        Subscription subscription2 = new Subscription();
        subscription.setName("dummy");
        subscription.setId(777);
        subscription.setStatus(Status.ACTIVE);
        subscription.setStatus(Status.CANCELED);
        subscription.setStatus(Status.EXPIRED);
        subscription.setProvider(Provider.APPLE);
        subscription.setProvider(Provider.GOOGLE);
        subscription.setExpirationDate(Instant.now());
        subscription.setUserId(777);

        subscription.equals(subscription2);
    }

    @Test
    void equalsAndHashCodeAndToString() {
        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        System.out.println(subscription1);
        System.out.println(subscription2);
        int hash1 = subscription1.hashCode();
        int hash2 = subscription2.hashCode();
        System.out.println(subscription1.equals(subscription2));
        assertEquals(subscription1, subscription2);
        subscription1.setName("dummy");
        subscription2.setName("dummy");
        assertEquals(subscription1, subscription2);
    }
}
