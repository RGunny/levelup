package me.rgunny.levelup.account.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountVersionJpaRepository extends JpaRepository<AccountVersionEntity, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select a from AccountVersionEntity a where a.id=:id")
    Optional<AccountVersionEntity> findByIdUsingOptimisticLock(@Param("id") Long id);

}
