package me.rgunny.levelup.transfer;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Transfer {

    private final Long id;
    private final Long amount;
    private final LocalDateTime createdDate;
    private final LocalDateTime endedDate;

    @Builder
    public Transfer(Long id, Long amount, LocalDateTime createdDate, LocalDateTime endedDate) {
        this.id = id;
        this.amount = amount;
        this.createdDate = LocalDateTime.now();
        this.endedDate = endedDate;
    }

    public static Transfer from(TransferCreate transferCreate) {
        return Transfer.builder()
                .id(transferCreate.getId())
                .amount(transferCreate.getAmount())
                .build();
    }
}
