package me.rgunny.levelup.user;

import java.util.NoSuchElementException;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void save(Member user) {
        memberRepository.save(user);
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ID 에 해당하는 User 가 없습니다."));
    }

    @Override
    public Member findByName(String name) {
        return memberRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Name 에 해당하는 User 가 없습니다."));
    }
}
