package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.request.UserSignInRequestDTO;
import com.example.todo.userapi.dto.request.UserSignUpRequestDTO;
import com.example.todo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다. - {}", email);
            return true;
        } else return false;
    }

    public UserSignUpResponseDTO create(final UserSignUpRequestDTO dto) throws Exception {
        String email = dto.getEmail();

        if (isDuplicate(email)) {
            throw new RuntimeException("중복된 이메일 입니다.");
        }

        // 패스워드 인코딩
        String encoded = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encoded);

        // dto를 User Entity로 변환해서 저장
        User saved = userRepository.save(dto.toEntity());
        log.info("회원 가입 정상 수행됨! - saved user - {}", saved);

        return new UserSignUpResponseDTO(saved);


    }

    public boolean checkLogin(UserSignInRequestDTO dto) throws Exception {
        String encoded = passwordEncoder.encode(dto.getPassword());

        User loginUser = null;
        try {
            loginUser = userRepository.findByEmail(dto.getEmail());
        } catch (NullPointerException e) {
            throw new RuntimeException("존재하지 않는 이메일 입니다.");
        }


        return loginUser.getPassword() == encoded; // 같으면 로그인 성공
        
    }
}
