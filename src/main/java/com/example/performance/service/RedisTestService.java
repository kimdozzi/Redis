package com.example.performance.service;

import com.example.performance.domain.Member;

public interface RedisTestService {
    void joinMember(Member member);
    Member updateMember(Member member, Long memberId);
    Member getMemberInfo(Long memberId);
    void removeMember(Long memberId);
}
