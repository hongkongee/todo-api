package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void insertData() {
        User u1 = User.builder()
                .email("abc@naver.com")
                .password("123123")
                .userName("김춘식")
                .build();

        userRepository.save(u1);
    }

    @Test
    @DisplayName("데이터 넣기")
    void addDatas() {
        // given
        User u1 = User.builder()
                .email("abc@naver.com")
                .password("123123")
                .userName("김춘식")
                .build();

        userRepository.save(u1);
        // when

        // then
    }

}