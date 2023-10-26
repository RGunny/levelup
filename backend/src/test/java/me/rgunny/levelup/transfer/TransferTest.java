package me.rgunny.levelup.transfer;

import me.rgunny.levelup.transfer.domain.Transfer;
import me.rgunny.levelup.transfer.domain.TransferCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TransferTest {

    @DisplayName("TransferCreate 로 Transfer 객체를 생성할 수 있다.")
    @Test
    void createTransferByTransferCreate(){
        // given
        TransferCreate transferCreate = TransferCreate.builder()
                .id(1L)
                .amount(10000L)
                .fromId(1L)
                .toId(2L)
                .build();

        // when
        Transfer transfer = Transfer.from(transferCreate);

        // then
        assertThat(transfer.getId()).isEqualTo(1L);
        assertThat(transfer.getAmount()).isEqualTo(10000L);
    }

    @DisplayName("fromId 와 toId 가 같을 시 IllegalArgumentException 예외가 발생한다.")
    @Test
    void test(){
        // given
        TransferCreate transferCreate = TransferCreate.builder()
                .id(1L)
                .amount(10000L)
                .fromId(1L)
                .toId(1L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> {
            Transfer.from(transferCreate);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
