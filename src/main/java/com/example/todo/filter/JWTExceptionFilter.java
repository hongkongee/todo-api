package com.example.todo.filter;

import com.example.todo.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// JWT 필터 관련 Exception 처리
@Component
@Slf4j
public class JWTExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        try {
            // 예외가 발생하지 않으면 필터를 통과
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 시 Auth Filter에서 예외가 발생 -> 앞에 있는 Exception Filter로 전달.
//            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
            log.warn("ExpiredJwtException 발생!");
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
//            request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
            log.warn("JwtException 발생!");
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.warn("토큰이 전달되지 않음!");
            setErrorResponse(response, ErrorCode.INVALID_AUTH);
        } catch (Exception e) {
            log.warn("알 수 없는 예외 발생!");
        }

    }

    // 필터에 예외가 발생하면 response 를 통해 브라우저로 응답하는 메서드
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        // 응답에 관한 여러가지 설정
        response.setStatus(errorCode.getHttpStatus().value()); // 응답 상태 코드 (401 or 403)
        response.setContentType("application/json; charset=UTF-8"); // HTTP content type : message body 에 들어가는 타입

        // (JSON 생성을 위한) Map 생성 및 데이터 추가
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", errorCode.toString());
        responseMap.put("code", errorCode.getHttpStatus());
        responseMap.put("korean", errorCode.getMessage());

        // Map을 JSON 문자열로 변환
        String jsonString = new ObjectMapper().writeValueAsString(responseMap);

        // JSON 데이터를 응답객체에 실어서 브라우저로 바로 응답.
        response.getWriter().write(jsonString);
    }
}
