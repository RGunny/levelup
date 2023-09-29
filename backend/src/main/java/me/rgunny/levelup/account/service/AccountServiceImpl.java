package me.rgunny.levelup.account.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import me.rgunny.levelup.account.common.domain.exception.ResourceNotFoundException;
import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.domain.AccountUpdate;
import me.rgunny.levelup.account.service.port.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Builder
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 ID 에 해당하는 계좌를 찾을 수 없습니다."));
    }

    @Transactional
    public Account create(AccountCreate accountCreate) {
        return accountRepository.save(Account.from(accountCreate));
    }

    @Transactional
    public Account update(Long id, AccountUpdate accountUpdate) {
        Account account = getById(id);
        account = account.update(accountUpdate);
        return accountRepository.save(account);
    }

    @Transactional
    public Account deposit(Long id, long amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accounts", id));
        account = account.deposit(amount);
        account = accountRepository.save(account);
        return account;
    }

    @Transactional
    public Account withdraw(Long id, long amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accounts", id));
        account = account.withdraw(amount);
        account = accountRepository.save(account);
        return account;
    }
    @Transactional
    public synchronized Account withdrawUsingSynchronized(Long id, long amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accounts", id));
        account = account.withdraw(amount);
        account = accountRepository.save(account);
        return account;
    }
}
