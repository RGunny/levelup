package me.rgunny.levelup.account.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AccountEntity a where a.id=:id")
    Optional<AccountEntity> findByIdUsingPessimisticLock(@Param("id") Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select a from AccountEntity a where a.id=:id")
    Optional<AccountEntity> findByIdUsingOptimisticLock(@Param("id") Long id);

}
