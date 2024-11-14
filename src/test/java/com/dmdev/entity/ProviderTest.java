package com.dmdev.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProviderTest {

    @ParameterizedTest
    @MethodSource("providerArgsSuccess")
    void findByNameSuccess(String testString, Provider provider) {
        assertEquals(Provider.findByName(testString), provider);
    }

    @ParameterizedTest
    @MethodSource("providerArgsInvalid")
    void shouldFindByNameThrowsNoSuchElementException(String testString) {
        assertThrows(NoSuchElementException.class, () -> Provider.findByName(testString));
    }

    private static Stream<Arguments> providerArgsInvalid() {
        return Stream.of(
                Arguments.of("palsdp"),
                Arguments.of("APpLEe"),
                Arguments.of("apPpLe"),
                Arguments.of("gosogle"),
                Arguments.of("GgooGle")
        );
    }

    private static Stream<Arguments> providerArgsSuccess() {
        return Stream.of(
                Arguments.of("APPLE", Provider.APPLE),
                Arguments.of("APpLE", Provider.APPLE),
                Arguments.of("apPLe", Provider.APPLE),
                Arguments.of("google", Provider.GOOGLE),
                Arguments.of("GooGle", Provider.GOOGLE)
        );
    }
}
