package me.rgunny.levelup.account.controller;

import lombok.RequiredArgsConstructor;
import me.rgunny.levelup.account.controller.port.AccountService;
import me.rgunny.levelup.account.controller.response.AccountResponse;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.domain.AccountUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody AccountCreate accountCreate) {
        Account account = accountService.create(accountCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AccountResponse.from(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(AccountResponse.from(accountService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> changeById(@PathVariable Long id, @RequestBody AccountUpdate accountUpdate) {
        return ResponseEntity.ok()
                .body(AccountResponse.from(accountService.update(id, accountUpdate)));
    }
}
