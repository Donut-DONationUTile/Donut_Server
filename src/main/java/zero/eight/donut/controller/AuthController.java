package zero.eight.donut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.auth.AuthRequestDto;
import zero.eight.donut.dto.auth.AuthTestDto;
import zero.eight.donut.service.AuthService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    // 기부자 구글 로그인
    @PostMapping("/giver/signin")
    public ApiResponse<?> googleSignIn() {
        return authService.googleSignIn(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization"));
    }

    @PostMapping("/giver/test")
    public ApiResponse<?> getGiverToken(@RequestBody AuthTestDto authTestDto) {
        log.info("입력된 값 -> {}", authTestDto);
        return authService.createGiverToken(authTestDto);
    }

    // 수혜자 회원가입
    @PostMapping("/receiver/signup")
    public ApiResponse<?> receiverSignUp(@RequestBody AuthRequestDto requestDto) {
        return authService.createAccount(requestDto);
    }

    // 수혜자 로그인
    @PostMapping("/receiver/signin")
    public ApiResponse<?> receiverSignIn(@RequestBody AuthRequestDto requestDto) {
        return authService.receiverSignIn(requestDto);
    }

}