package me.rgunny.levelup.user;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    void save(Member user);

    Member findById(Long id);

    Member findByName(String name);
}
