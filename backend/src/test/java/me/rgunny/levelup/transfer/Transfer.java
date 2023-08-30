package me.rgunny.levelup.transfer;

import java.time.LocalDateTime;

public class Transfer {

    private Long id;

    private Long amount;

    private LocalDateTime createdDate;

    public Transfer() {
    }

    public Transfer(Long id, Long amount, LocalDateTime createdDate) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
    }


}
