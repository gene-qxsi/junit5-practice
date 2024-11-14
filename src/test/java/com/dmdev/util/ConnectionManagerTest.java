package com.dmdev.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConnectionManagerTest {

    MockedStatic<PropertiesUtil> propertiesStatic;
    MockedStatic<DriverManager> driverStatic;

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";

    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DRIVER = "org.h2.Driver";

    private static final String INVALID_URL = "invalid:url";

    @BeforeEach
    void open() {
        propertiesStatic = mockStatic(PropertiesUtil.class);
        driverStatic = mockStatic(DriverManager.class);
    }

    @AfterEach
    void close() {
        propertiesStatic.close();
        driverStatic.close();
    }

    @Test
    @SneakyThrows
    void getSuccess() {
        try (Connection connectionMock = mock(Connection.class)) {
            propertiesStatic.when(() -> PropertiesUtil.get(URL_KEY)).thenReturn(URL);
            propertiesStatic.when(() -> PropertiesUtil.get(USER_KEY)).thenReturn(USER);
            propertiesStatic.when(() -> PropertiesUtil.get(PASSWORD_KEY)).thenReturn(PASSWORD);
            propertiesStatic.when(() -> PropertiesUtil.get(DRIVER_KEY)).thenReturn(DRIVER);

            driverStatic.when(() -> DriverManager.getConnection(URL, USER, PASSWORD)).thenReturn(connectionMock);

            Connection actual = ConnectionManager.get();
            assertEquals(actual, connectionMock);

            driverStatic.verify(() -> DriverManager.getConnection(URL, USER, PASSWORD), times(1));
            propertiesStatic.verify(() -> PropertiesUtil.get(URL_KEY), times(1));
            propertiesStatic.verify(() -> PropertiesUtil.get(USER_KEY), times(1));
            propertiesStatic.verify(() -> PropertiesUtil.get(PASSWORD_KEY), times(1));
        }
    }

    @Test
    void shouldGetThrowSQLException() {

        propertiesStatic.when(() -> PropertiesUtil.get(URL_KEY)).thenReturn(INVALID_URL);
        propertiesStatic.when(() -> PropertiesUtil.get(USER_KEY)).thenReturn(USER);
        propertiesStatic.when(() -> PropertiesUtil.get(PASSWORD_KEY)).thenReturn(PASSWORD);
        propertiesStatic.when(() -> PropertiesUtil.get(DRIVER_KEY)).thenReturn(DRIVER);

        SQLException expectedException = new SQLException("Invalid connection parameters");
        driverStatic.when(() -> DriverManager.getConnection(INVALID_URL, USER, PASSWORD)).thenThrow(expectedException);

        assertThrows(expectedException.getClass(), ConnectionManager::get);

        driverStatic.verify(() -> DriverManager.getConnection(INVALID_URL, USER, PASSWORD), times(1));
        propertiesStatic.verify(() -> PropertiesUtil.get(URL_KEY), times(1));
        propertiesStatic.verify(() -> PropertiesUtil.get(USER_KEY), times(1));
        propertiesStatic.verify(() -> PropertiesUtil.get(PASSWORD_KEY), times(1));
    }
}
