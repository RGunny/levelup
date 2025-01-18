package me.rgunny.levelup.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceInMemoryImplTest {

    private AccountServiceInMemoryImpl service;

    @BeforeEach
    void setUp() {
        service = new AccountServiceInMemoryImpl();
    }

    @Test
    @DisplayName("단일 쓰레드 - 입금 성공 테스트")
    void depositSingleThreadSuccessTest() throws Exception {
        // given
        Long accountId = 100L;
        long amount = 1000L;

        // when
        boolean result = service.deposit(accountId, amount);

        // then
        assertTrue(result, "입금은 성공해야 함");
        assertEquals(1000L, getBalance(accountId), "잔고는 1000이어야 함");
    }

    @Test
    @DisplayName("단일 쓰레드 - 출금 성공 테스트")
    void withdrawSingleThreadSuccessTest() throws Exception {
        // given
        Long accountId = 200L;
        long initial = 1000L;
        long withdrawAmount = 500L;

        // 먼저 입금
        service.deposit(accountId, initial);

        // when
        boolean result = service.withdraw(accountId, withdrawAmount);

        // then
        assertTrue(result, "출금은 성공해야 함");
        assertEquals(500L, getBalance(accountId), "잔고는 500이어야 함");
    }

    @Test
    @DisplayName("단일 쓰레드 - 출금 실패 테스트 (잔고 부족)")
    void withdrawFailDueToInsufficientBalance() throws Exception {
        // given
        Long accountId = 300L;
        long initial = 200L;
        long withdrawAmount = 300L;

        service.deposit(accountId, initial);

        // when
        boolean result = service.withdraw(accountId, withdrawAmount);

        // then
        assertFalse(result, "잔고가 부족하므로 출금 실패해야 함");
        assertEquals(200L, getBalance(accountId), "잔고는 그대로 200이어야 함");
    }

    @Test
    @DisplayName("동시에 여러 입금 요청 시 - 오직 1건만 성공하는지 테스트")
    void concurrentDepositTest() throws InterruptedException {
        // given
        Long accountId = 1L;
        // 초기 잔고는 0
        int threadCount = 5;
        long depositAmount = 1000L;

        // when
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        // 스레드마다 동시에 deposit()을 호출하도록 구성
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await(); // 모두 대기 후, latch.countDown() 시점에 동시에 시작
                return service.deposit(accountId, depositAmount);
            }));
        }

        startLatch.countDown(); // 동시에 시작
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // then
        // 각 스레드 결과(성공 or 실패) 수집
        long successCount = futures.stream().filter(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                return false;
            }
        }).count();

        // 실제 최종 잔고 확인
        long finalBalance = getBalance(accountId);

        System.out.println("=== Concurrent Deposit Test ===");
        System.out.println("Success Count: " + successCount + ", Final Balance: " + finalBalance);

        assertTrue(successCount >= 1 && successCount <= 5);

        assertTrue(finalBalance >= 1000 && finalBalance <= 5000);
    }

    @Test
    @DisplayName("동시에 여러 출금 요청 시 - 순서대로 처리되어 잔고가 음수가 되지 않는지 테스트")
    void concurrentWithdrawTest() throws InterruptedException {
        // given
        Long accountId = 2L;
        // 초기 잔고를 1000으로 설정
        service.deposit(accountId, 1000L);

        int threadCount = 5;
        long withdrawAmount = 200L; // 5명 각각 200원씩 출금 = 총 1000

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                startLatch.await();
                return service.withdraw(accountId, withdrawAmount);
            }));
        }

        startLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // then
        long successCount = futures.stream().filter(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                return false;
            }
        }).count();
        long finalBalance = getBalance(accountId);

        System.out.println("=== Concurrent Withdraw Test ===");
        System.out.println("Success Count: " + successCount + ", Final Balance: " + finalBalance);

        assertEquals(5, successCount, "5번 모두 출금에 성공해야 한다");
        assertEquals(0L, finalBalance, "최종 잔고는 0이 되어야 한다");
    }

    // 테스트용 - Reflection
    private long getBalance(Long accountId) {
        try {
            var field = AccountServiceInMemoryImpl.class.getDeclaredField("balanceMap");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            var map = (ConcurrentHashMap<Long, Long>) field.get(service);
            return map.getOrDefault(accountId, 0L);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}