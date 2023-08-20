package me.rgunny.levelup.user;

import java.util.NoSuchElementException;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository memberRepository) {
        this.userRepository = memberRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ID 에 해당하는 User 가 없습니다."));
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Name 에 해당하는 User 가 없습니다."));
    }
}
