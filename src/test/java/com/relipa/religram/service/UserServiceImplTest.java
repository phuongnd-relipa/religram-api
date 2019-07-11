package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import com.relipa.religram.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserServiceImpl.class)
//@SpringBootTest
public class UserServiceImplTest {

    private static final String USER_EXIST = "userExist";
    private static final String EMAIL_EXIST = "emailExist";

    private User user;

    private UserServiceImpl userService;

    @InjectMocks
    private UserRepository userRepository;


    @Before
    public void initialize() {
        this.user = User.UserBuilder.builder()
                .id(1L)
                .email("tetser@relipasoft.com")
                .fullName("Relipa Software")
                .username("relipa")
                .password("123456")
                .build();
    }

    @Test
    public void testRegisterNonExistedUser () throws Exception {
        // TODO
        userService = new UserServiceImpl(userRepository);

        UserServiceImpl spy = PowerMockito.spy(userService);
        PowerMockito.doReturn(true).when(spy, USER_EXIST);
        PowerMockito.doReturn(true).when(spy, EMAIL_EXIST);

    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegisterExistedUsername() {
        // TODO
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegisterExistedEmail() {
        // TODO
    }
}
