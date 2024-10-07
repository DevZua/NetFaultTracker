package com.netcoretech.netfaulttracker.repository;

import com.netcoretech.netfaulttracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자 이름으로 사용자를 찾음
     *
     * @param username 찾고자 하는 사용자의 사용자 이름
     * @return 찾은 사용자의 Optional 객체
     */
    Optional<User> findByUsername(String username);

    /**
     * 주어진 사용자 이름이 이미 존재하는지 확인
     *
     * @param username 확인하고자 하는 사용자 이름
     * @return 사용자 이름이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByUsername(String username);

    /**
     * 주어진 이메일 주소가 이미 존재하는지 확인
     *
     * @param email 확인하고자 하는 이메일 주소
     * @return 이메일 주소가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByEmail(String email);
}