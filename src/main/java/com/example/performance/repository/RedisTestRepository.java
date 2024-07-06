package com.example.performance.repository;

import com.example.performance.domain.Member;

public interface RedisTestRepository {
    Member save(Member member);
    Member findOne(Long memberId);
    void remove(Member member);
}
