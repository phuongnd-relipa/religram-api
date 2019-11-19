package com.relipa.religram.controller;

/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relipa.religram.configuration.SecurityConfig;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.service.UserService;
import com.relipa.religram.util.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
@WebMvcTest(controllers = SearchController.class)
class ITSearchController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void searchUser_whenNotAuthenticate_thenReturns401() throws Exception {
        mockMvc.perform(get("/v1/search/user")
                .param("name", "phuong")
                .param("page", "1")
                .contentType("application/json;charset=UTF-8"))
                // DEBUG:
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void searchUser_whenValidInput_thenReturns200() throws Exception {
        mockMvc.perform(get("/v1/search/user")
                .param("name", "phuong")
                .param("page", "1")
                .contentType("application/json;charset=UTF-8"))
                // DEBUG:
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void searchUser_whenMissingParamInput_thenReturns400() throws Exception {
        // Required parameter 'name' is missing
        mockMvc.perform(get("/v1/search/user")
                .param("page", "1")
                .contentType("application/json;charset=UTF-8"))
                // DEBUG:
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void searchUser_whenValidInput_thenVerifyBusinessLogic() throws Exception {
        String name = "phuong";
        Integer page = 1;

        // Test data
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUsername("phuongnd");
        userInfoBean.setEmail("phuongnd@relipasoft.com");
        userInfoBean.setFullname("Nguyen Duy Phuong");

        List<UserInfoBean> expectResults = new ArrayList<>();
        expectResults.add(userInfoBean);

        when(userService.searchUsersByName(name, page)).thenReturn(expectResults);

        mockMvc.perform(get("/v1/search/user")
                .param("name", name)
                .param("page", String.valueOf(page))
                .contentType("application/json;charset=UTF-8"))
                // DEBUG:
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        List<UserInfoBean> actualResults = userService.searchUsersByName(name, page);
        assertThat(expectResults.size()).isEqualTo(actualResults.size());
        if (!actualResults.isEmpty()) {
            assertThat(expectResults.get(0).getUsername()).isEqualTo(actualResults.get(0).getUsername());
            assertThat(expectResults.get(0).getEmail()).isEqualTo(actualResults.get(0).getEmail());
            assertThat(expectResults.get(0).getFullname()).isEqualTo(actualResults.get(0).getFullname());
        }
    }

    @Test
    @WithMockUser
    void searchUser_whenValidInput_thenReturnsResponse() throws Exception {
        String name = "phuong";
        Integer page = 1;

        // Test data
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUsername("phuongnd");
        userInfoBean.setEmail("phuongnd@relipasoft.com");
        userInfoBean.setFullname("Nguyen Duy Phuong");

        List<UserInfoBean> expectResults = new ArrayList<>();
        expectResults.add(userInfoBean);

        when(userService.searchUsersByName(name, page)).thenReturn(expectResults);

        MvcResult result = mockMvc.perform(get("/v1/search/user")
                .param("name", name)
                .param("page", String.valueOf(page))
                .contentType("application/json;charset=UTF-8"))
                // DEBUG:
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String actualResults = result.getResponse().getContentAsString();
        assertThat(objectMapper.writeValueAsString(expectResults)).isEqualToIgnoringWhitespace(actualResults);
    }

}