package me.rgunny.levelup.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountUpdate {

    private final String number; // bank account number;
    private final String password;
    private final String name;


}
