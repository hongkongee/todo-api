package com.example.todo.userapi.api;

import com.example.todo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    // 이메일 중복 확인 요청 처리
    // GET: /api/auth/check?email=zzzz@xxx.com
    // jpa는 pk로 조회하는 메서드는 기본 제공되지만, 다른 컬럼으로 조회하는 메서드는 제공되지 않습니다.
    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam("email") String email) {
        log.info("/api/auth?email={}", email);

        if (email.trim().isEmpty()) {
            return ResponseEntity.badRequest() // 400 error
                    .body("이메일을 전달해 주세요!");
        }

        boolean resultFlag = userService.isDuplicate(email);

        log.info("중복? - {}", resultFlag);

        return ResponseEntity.ok().body(resultFlag);
    }
}
