package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static com.dmdev.dto.CreateSubscriptionDto.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateSubscriptionValidatorTest {

    @ParameterizedTest
    @MethodSource("args")
    void validate(CreateSubscriptionDto dto, ValidationResult expectedResult) {
        CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

        ValidationResult actualResult = validator.validate(dto);

        assertEquals(expectedResult.getErrors(), actualResult.getErrors());
    }


    private static Stream<Arguments> args() {
        ValidationResult valid = new ValidationResult();

        ValidationResult invalid = new ValidationResult();
        invalid.add(Error.of(100, "userId is invalid"));
        invalid.add(Error.of(101, "name is invalid"));
        invalid.add(Error.of(102, "provider is invalid"));
        invalid.add(Error.of(103, "expirationDate is invalid"));

        ValidationResult error100 = new ValidationResult();
        error100.add(Error.of(100, "userId is invalid"));

        ValidationResult error101 = new ValidationResult();
        error101.add(Error.of(101, "name is invalid"));

        ValidationResult error102 = new ValidationResult();
        error102.add(Error.of(102, "provider is invalid"));

        ValidationResult error103 = new ValidationResult();
        error103.add(Error.of(103, "expirationDate is invalid"));

        return Stream.of(
                Arguments.of(builder().userId(1).name("Ivan").expirationDate(Instant.now().plusSeconds(10000))
                        .provider("apple").build(), valid),
                Arguments.of(builder().userId(null).name("Ivan").expirationDate(Instant.now().plusSeconds(10000))
                        .provider("apple").build(), error100),
                Arguments.of(builder().userId(1).name("    ").expirationDate(Instant.now().plusSeconds(10000))
                        .provider("apple").build(), error101),
                Arguments.of(builder().userId(1).name("Ivan").expirationDate(Instant.now().plusSeconds(10000))
                        .provider("fake").build(), error102),
                Arguments.of(builder().userId(1).name("Ivan").expirationDate(null)
                        .provider("google").build(), error103),
                Arguments.of(builder().userId(1).name("Ivan").expirationDate(Instant.now().minusSeconds(10000))
                        .provider("google").build(), error103),
                Arguments.of(builder().userId(null).name("   ").expirationDate(null)
                        .provider("fake").build(), invalid)
        );
    }

}
