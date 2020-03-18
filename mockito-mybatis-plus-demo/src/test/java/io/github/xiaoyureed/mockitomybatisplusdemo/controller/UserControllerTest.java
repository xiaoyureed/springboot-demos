package io.github.xiaoyureed.mockitomybatisplusdemo.controller;

import io.github.xiaoyureed.mockitomybatisplusdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author xiaoyu
 * date: 2020/1/24
 */
@SpringBootTest
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    /**
     * spring-test 提供
     */
    private MockMvc mockMvc;

    @BeforeEach
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLogin() throws Exception {
        when(userService.login(any(String.class), any(String.class))).then(invocation -> "12345");
        mockMvc.perform(post("/user/session")
                // .header()
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"name\": \"Jack\",\n" +
                        "  \"pwd\": \"abd\"\n" +
                        "}")
        ).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"code\": \"0\",\n" +
                        "  \"msg\": \"\",\n" +
                        "  \"data\": \"12345\"\n" +
                        "}"));
    }
}
