package me.rgunny.levelup.account.transfer.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Transfer {

    private final Long id;
    private final Long amount;
    private final Long fromId;
    private final Long toId;
    private final LocalDateTime createdDate;
    private final LocalDateTime endedDate;

    @Builder
    public Transfer(Long id, Long amount, Long fromId, Long toId, LocalDateTime createdDate, LocalDateTime endedDate) {
        this.id = id;
        this.amount = amount;
        this.fromId = fromId;
        this.toId = toId;
        this.createdDate = createdDate;
        this.endedDate = endedDate;
    }

    public static Transfer from(TransferCreate transferCreate) {
        if (transferCreate.getFromId() == transferCreate.getToId()) {
            throw new IllegalArgumentException("입급계좌와 출금계좌가 같을 수 없습니다.");
        }
        return Transfer.builder()
                .id(transferCreate.getId())
                .amount(transferCreate.getAmount())
                .fromId(transferCreate.getFromId())
                .toId(transferCreate.getToId())
                .build();
    }

}
