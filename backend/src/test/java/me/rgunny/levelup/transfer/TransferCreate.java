package me.rgunny.levelup.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransferCreate {

    private final Long id;
    private final Long amount;
    private final LocalDateTime createdDate;

    public TransferCreate(Long id, Long amount, LocalDateTime createdDate) {
        this.id = id;
        this.amount = amount;
        this.createdDate = LocalDateTime.now();
    }
}
