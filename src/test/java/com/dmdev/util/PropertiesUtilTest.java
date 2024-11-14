package com.dmdev.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Properties;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PropertiesUtilTest {

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";

    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static final String PASSWORD_INVALID_KEY = "db.dummy";
    private static final String USER_INVALID_KEY = "db.invalid";
    private static final String URL_INVALID_KEY = "db.WHY";

    @ParameterizedTest
    @MethodSource("getValues")
    void get(String key, String expectedResult) {
        Properties mock = mock(Properties.class);
        doReturn(expectedResult).when(mock).getProperty(key);

        String actualResult = PropertiesUtil.get(key);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getValues() {
        return Stream.of(
                Arguments.of(URL_INVALID_KEY, null),
                Arguments.of(USER_INVALID_KEY, null),
                Arguments.of(PASSWORD_INVALID_KEY, null),
                Arguments.of(URL_KEY, URL),
                Arguments.of(USER_KEY, USER),
                Arguments.of(PASSWORD_KEY, PASSWORD)
        );
    }

}





















