package me.rgunny.levelup.medium.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.rgunny.levelup.account.controller.AccountController;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.service.port.AccountRepository;
import me.rgunny.levelup.medium.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();

        Account account = Account.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌")
                .balance(0L)
                .build();
        Account account2 = Account.builder()
                .id(2L)
                .number("2-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌2")
                .balance(10000L)
                .build();
        Account account3 = Account.builder()
                .id(3L)
                .number("3-1234-5678-9")
                .password("1q2w3e4r!@")
                .name("나라사랑계좌3")
                .balance(50000L)
                .build();

        accountRepository.save(account);
        accountRepository.save(account2);
        accountRepository.save(account3);
    }

    @DisplayName("계좌를 생성할 수 있다.")
    @Test
    void createAccountSuccessTest() throws Exception {
        // given
        AccountCreate accountCreate = AccountCreate.builder()
                .id(1L)
                .number("0-1234-5678-9")
                .name("나라사랑계좌")
                .password("1q2w3e4r!@")
                .build();

        // when
        // then
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreate)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("0-1234-5678-9"))
                .andExpect(jsonPath("$.password").value("1q2w3e4r!@"))
                .andExpect(jsonPath("$.name").value("나라사랑계좌"))
                .andExpect(jsonPath("$.balance").value(0));

    }

    @DisplayName("계좌를 id로 조회할 수 있다.")
    @Test
    void getAccountByIdSuccessTest() throws Exception {
        // given

        // when
        // then
        mockMvc.perform(get("/api/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("0-1234-5678-9"))
                .andExpect(jsonPath("$.password").value("1q2w3e4r!@"))
                .andExpect(jsonPath("$.name").value("나라사랑계좌"))
                .andExpect(jsonPath("$.balance").value(0));

    }

    @DisplayName("존재하지 않는 id로 계좌를 조회할 경우 404 응답을 받는다.")
    @Test
    void getAccountByNotExistIdOccur404Exception() throws Exception {
        // given

        // when
        // then
        mockMvc.perform(get("/api/accounts/12345679"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("[RESOURCE_NOT_FOUND_EXCEPTION] Accounts에서 ID12345679 을 찾을 수 없습니다."));
    }

}
