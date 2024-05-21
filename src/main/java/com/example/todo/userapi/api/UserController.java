package com.example.todo.userapi.api;

import com.example.todo.userapi.dto.request.UserSignInRequestDTO;
import com.example.todo.userapi.dto.request.UserSignUpRequestDTO;
import com.example.todo.userapi.dto.response.LoginResponseDTO;
import com.example.todo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.todo.userapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 회원 가입 요청 처리
    // POST: /api/auth
    @PostMapping
    public ResponseEntity<?> signUp(
            @Validated @RequestBody UserSignUpRequestDTO dto,
            BindingResult result
    ) {
        log.info("/api/auth POST - {}", dto);

        ResponseEntity<List<FieldError>> resultEntity = getFieldErrorResponseEntity(result);
        if (resultEntity != null) return resultEntity;

        try {
            UserSignUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    

    // 로그인 요청 처리 메서드를 선언하세요. POST: /api/auth/signin
    // LoginRequestDTO 클래스를 생성해서 요청 값을 받아주세요.
    // 서비스로 넘겨서, 로그인 유효성을 검증하세요 (비밀번호 암호화 되어 있어요)
    // 로그인 결과를 응답 상태 코드로 구분해서 보내 주세요.
    // 로그인이 성공했다면 200, 실패라면 400을 보내주세요 (에러 메세지를 상황에 따라 다르게 전달해 주세요)
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @Validated @RequestBody UserSignInRequestDTO dto,
            BindingResult result
    ) {
        log.info("/api/auth/signin: POST! - {}", dto);

        ResponseEntity<List<FieldError>> response = getFieldErrorResponseEntity(result);
        if (response != null) return response;

        try {
            LoginResponseDTO responseDTO = userService.authenticate(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    // 유효성 검사 메서드
    private static ResponseEntity<List<FieldError>> getFieldErrorResponseEntity(BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return null;
    }

}
