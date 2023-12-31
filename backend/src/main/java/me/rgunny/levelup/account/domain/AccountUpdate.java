package me.rgunny.levelup.account.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountUpdate {

    private final Long id;
    private final String number; // bank account number;
    private final String password;
    private final String name;


}
