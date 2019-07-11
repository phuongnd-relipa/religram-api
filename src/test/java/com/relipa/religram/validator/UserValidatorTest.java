package com.relipa.religram.validator;

import com.relipa.religram.validator.UserValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@SpringBootTest
public class UserValidatorTest {

    private UserValidator userValidator;
    private String input;
    private boolean expected;

    @Mock
    private ConstraintValidatorContext context;

    @Before
    public void initialize() {
        // TODO
        this.userValidator = new UserValidator();
    }

    public UserValidatorTest(String input, boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "Username pattern: {index}")
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][] {
                { "test", true },
                { "123", true },
                { "test123", true },
                { "Test", false },
                { "!@#$", false },
                { "" , false}
        });
    }

    @Test
    public void testUsername() {
        Assert.assertEquals(this.userValidator.isValid(input, context), expected);

    }

}
