package me.rgunny.levelup.account.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountCreate {

    private final Long id;
    private final String number;
    private final String password;
    private final String name;

    @Builder
    public AccountCreate(@JsonProperty("id") Long id,
                         @JsonProperty("number") String number,
                         @JsonProperty("password") String password,
                         @JsonProperty("name") String name) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.name = name;
    }
}
