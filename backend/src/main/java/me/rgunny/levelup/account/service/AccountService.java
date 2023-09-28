package me.rgunny.levelup.account.service;

import me.rgunny.levelup.account.domain.Account;
import me.rgunny.levelup.account.domain.AccountCreate;
import me.rgunny.levelup.account.domain.AccountUpdate;

public interface AccountService {

    Account getById(Long id);

    Account create(AccountCreate accountCreate);

    Account update(Long id, AccountUpdate accountUpdate);

    Account deposit(Long id, long amount);

    Account withdraw(Long id, long amount);
}
