package me.rgunny.levelup.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void save(User user);

    User findById(Long id);

    User findByName(String name);
}
