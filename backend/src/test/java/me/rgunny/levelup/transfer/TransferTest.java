package me.rgunny.levelup.transfer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferTest {

    @DisplayName("TransferRequest 로 Transfer 객체를 생성할 수 있다.")
    @Test
    void test(){
        // given
        TransferCreate transferCreate = TransferCreate.builder()
                .id(1L)
                .amount(10000L)
                .build();

        // when
        Transfer transfer = Transfer.from(transferCreate);

        // then
        assertThat(transfer.getId()).isEqualTo(1L);
        assertThat(transfer.getAmount()).isEqualTo(10000L);
    }
}
