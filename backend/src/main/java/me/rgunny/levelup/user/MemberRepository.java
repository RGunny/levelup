package me.rgunny.levelup.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select u from Member u where u.name =:name")
    Optional<Member> findByName(String name);

}
