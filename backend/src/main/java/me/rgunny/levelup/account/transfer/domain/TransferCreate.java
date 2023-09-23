package me.rgunny.levelup.account.transfer.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransferCreate {

    private final Long id;
    private final Long amount;
    private final Long fromId;
    private final Long toId;
    private final LocalDateTime createdDate;

    public TransferCreate(Long id, Long amount, Long fromId, Long toId, LocalDateTime createdDate) {
        this.id = id;
        this.amount = amount;
        this.fromId = fromId;
        this.toId = toId;
        this.createdDate = createdDate;
    }
}
