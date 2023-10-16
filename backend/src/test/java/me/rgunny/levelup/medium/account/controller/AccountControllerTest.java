package me.rgunny.levelup.medium.account.controller;

import me.rgunny.levelup.account.controller.AccountController;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.service.port.AccountRepository;
import me.rgunny.levelup.medium.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MockMvc mockMvc;

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

    @DisplayName("계좌를 id로 조회할 수 있다.")
    @Test
    void getAccountByIdSuccessTest() throws Exception {
        // given

        // when
        // then
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("0-1234-5678-9"))
                .andExpect(jsonPath("$.password").value("1q2w3e4r!@"))
                .andExpect(jsonPath("$.name").value("나라사랑계좌"))
                .andExpect(jsonPath("$.balance").value(0L));

    }

}
