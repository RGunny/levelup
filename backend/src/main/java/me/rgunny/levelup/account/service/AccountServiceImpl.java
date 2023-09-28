package me.rgunny.levelup.account.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.rgunny.levelup.account.common.domain.exception.ResourceNotFoundException;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.domain.AccountUpdate;
import me.rgunny.levelup.account.service.port.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Builder
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 ID 에 해당하는 계좌를 찾을 수 없습니다."));
    }

    public Account create(AccountCreate accountCreate) {
        return accountRepository.save(Account.from(accountCreate));
    }

    public Account update(Long id, AccountUpdate accountUpdate) {
        Account account = getById(id);
        account = account.update(accountUpdate);
        return accountRepository.save(account);
    }

    public Account deposit(Long id, long amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accounts", id));
        account = account.deposit(amount);
        account = accountRepository.save(account);
        return account;
    }

    public synchronized Account withdraw(Long id, long amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accounts", id));
        account = account.withdraw(amount);
        account = accountRepository.save(account);
        return account;
    }
}
