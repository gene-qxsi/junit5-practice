package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionDaoIT extends IntegrationTestBase {

    private static final SubscriptionDao dao = SubscriptionDao.getInstance();

    @Test
    void findAll() {
        Subscription subscription1 = getSubscription();
        Subscription subscription2 = getSubscription();
        subscription2.setUserId(2);
        dao.insert(subscription1);
        dao.insert(subscription2);

        List<Integer> actualResult = dao.findAll().stream().map(Subscription::getUserId).toList();

        assertThat(actualResult).contains(subscription1.getUserId(), subscription2.getUserId());
    }

    @Test
    void findById() {
        Subscription subscription = getSubscription();
        dao.insert(subscription);

        Optional<Subscription> actualResult = dao.findById(subscription.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(subscription.getId());
    }

    @Test
    void delete() {
        Subscription subscription = getSubscription();
        dao.insert(subscription);

        boolean actualResult = dao.delete(subscription.getId());

        assertTrue(actualResult);
        assertThat(dao.findAll().stream().map(Subscription::getId)).doesNotContain(subscription.getId());
    }

    @Test
    void update() {
        Subscription subscription = getSubscription();
        dao.insert(subscription);
        Subscription subscription1 = getSubscription();
        subscription1.setStatus(Status.CANCELED);
        subscription1.setName("Nemo");
        subscription1.setId(subscription.getId());

        Subscription actualResult = dao.update(subscription1);
        assertThat(subscription.getId()).isEqualTo(actualResult.getId());
        assertThat(subscription.getName()).isNotEqualTo(actualResult.getName());
        assertThat(subscription.getStatus()).isNotEqualTo(actualResult.getStatus());
        assertThat(subscription.getUserId()).isEqualTo(actualResult.getUserId());
        assertThat(subscription.getProvider()).isEqualTo(actualResult.getProvider());
    }

    @Test
    void insert() {
        Subscription subscription = getSubscription();

        Subscription actualResult = dao.insert(subscription);

        assertThat(actualResult.getId()).isEqualTo(subscription.getId());
        assertThat(dao.findById(subscription.getId())).isPresent();
    }

    @Test
    void findByUserId() {
        Subscription subscription1 = getSubscription();
        Subscription subscription2 = getSubscription();
        subscription2.setName("dummy2");
        Subscription subscription3 = getSubscription();
        subscription3.setName("dummy3");
        dao.insert(subscription1);
        dao.insert(subscription2);
        dao.insert(subscription3);

        List<Integer> actualResult = dao.findByUserId(subscription1.getUserId()).stream().map(Subscription::getId).toList();

        assertThat(actualResult).contains(subscription1.getId(), subscription2.getId(), subscription3.getId());
    }

    private Subscription getSubscription() {
        return Subscription.builder()
                .id(1)
                .status(Status.ACTIVE)
                .expirationDate(Instant.now().plusSeconds(10000))
                .provider(Provider.APPLE)
                .name("dummy1")
                .userId(1)
                .build();
    }
}
