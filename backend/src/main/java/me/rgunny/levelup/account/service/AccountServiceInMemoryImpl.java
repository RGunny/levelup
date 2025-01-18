package me.rgunny.levelup.account.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccountServiceInMemoryImpl {

    private final ConcurrentHashMap<Long, Long> balanceMap = new ConcurrentHashMap<>();

    // 계좌별 락(공정성 옵션 true로 하면 요청 순서대로 처리 가능)
    private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    /**
     * 입금: 동시에 여러 요청이 오면, 가장 먼저 lock을 얻은 1건만 성공하고, 나머지는 실패 처리
     */
    public boolean deposit(Long accountId, long amount) {
        ReentrantLock lock = getLockForAccount(accountId);

        // ★ tryLock() → 만약 lock을 이미 누군가 쥐고 있다면 false 리턴
        boolean isLocked = lock.tryLock();
        if (!isLocked) {
            // 동시에 들어온 다른 입금 요청이 이미 lock을 잡았다면,
            // 이 요청은 실패 처리
            return false;
        }

        try {
            // 여기까지 오면 lock을 얻음 → 입금 진행
            long currentBalance = balanceMap.getOrDefault(accountId, 0L);
            long newBalance = currentBalance + amount;
            balanceMap.put(accountId, newBalance);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 출금: 동시에 여러 요청이 오면 순서대로 실행
     *       => lock()은 블로킹이므로 먼저 호출된 스레드가 끝나야 다음 스레드가 들어옴
     */
    public boolean withdraw(Long accountId, long amount) {
        ReentrantLock lock = getLockForAccount(accountId);

        // ★ lock() → 이미 누가 점유중이면 대기(순서대로 처리)
        lock.lock();
        try {
            long currentBalance = balanceMap.getOrDefault(accountId, 0L);

            // 잔고 부족하면 실패
            if (currentBalance < amount) {
                return false;
            }

            long newBalance = currentBalance - amount;
            balanceMap.put(accountId, newBalance);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * ReentrantLock을 캐싱 & 가져오기
     */
    private ReentrantLock getLockForAccount(Long accountId) {
        return lockMap.computeIfAbsent(accountId,
                k -> new ReentrantLock(true)  // 공정 모드(true) → 스레드 대기열이 들어온 순서대로 lock 획득
        );
    }

}
