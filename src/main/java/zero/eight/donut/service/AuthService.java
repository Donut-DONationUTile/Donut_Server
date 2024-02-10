package zero.eight.donut.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.JwtUtils;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.dto.auth.LoginResponseDto;
import zero.eight.donut.dto.auth.MemberDto;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.InternalServerErrorException;
import zero.eight.donut.exception.Success;
import zero.eight.donut.exception.UnauthorizedException;
import zero.eight.donut.repository.GiverRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////CLIENT ID 수정 필요////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static String CLIENT_ID = "";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final GiverRepository giverRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public ApiResponse<?> googleLogin(String idToken) {

        GoogleIdToken tokenDto = verifyEmail(idToken);

        // 토큰 유효성 검사
        if (tokenDto == null) {
            return ApiResponse.failure(Error.INVALID_GOOGLE_TOKEN);
        }

        // ID 토큰에서 프로필 정보 열람
        GoogleIdToken.Payload payload = tokenDto.getPayload();
//        String userId = payload.getSubject();  // Use this value as a key to identify a user.
        String email = payload.getEmail();

        // 이메일로 Giver 탐색
        Optional<Giver> giver = giverRepository.findByEmail(email);

        // 신규 사용자면 회원가입(닉네임, 이메일, 생성시간, 수정시간)
        giver.ifPresentOrElse(
                // Optional 객체가 값이 있는 경우 실행할 동작
                existingGiver -> {
                    log.info("기가입유저 -> {}", existingGiver);
                },
                // Optional 객체가 비어있는 경우 실행할 동작
                () -> {
                    log.info("신규 가입 유저 -> {}");
                    googleSignin(email);
                }
        );


        giver = giverRepository.findByEmail(email);

        MemberDto member = MemberDto.builder()
                .email(giver.get().getEmail())
                .role(Role.ROLE_GIVER)
                .build();

        // 불필요한 정보
//        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//        String name = (String) payload.get("name");
//        String pictureUrl = (String) payload.get("picture");
//        String locale = (String) payload.get("locale");
//        String familyName = (String) payload.get("family_name");
//        String givenName = (String) payload.get("given_name");

        // 사용자 이메일 & 역할로 jwt 생성
        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(member))
                .refreshtoken(jwtUtils.createRefreshToken(member))
                .name(giver.get().getName())
                .build();

        return ApiResponse.success(Success.LOGIN_SUCCESS, loginResponse);
    }

    @Transactional
    private void googleSignin(String email) {
        String name = getNameFromEmail(email);
        Giver giver = Giver.builder()
                .name(name)
                .email(email)
                // createdAt, updatedAt
                .build();

        giverRepository.save(giver);
    }

    private GoogleIdToken verifyEmail(String googleToken) {

        GoogleIdToken idToken;
        
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        try {
            idToken = verifier.verify(googleToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Print user identifier
                String googleId = payload.getSubject();
                log.info("User ID: " + googleId);

                // Get profile information from payload
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

                // 불필요 정보
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

                if (!emailVerified) {
                    return null;
                }

            } else {
                log.info("Invalid Google token");
                // exception
                throw new UnauthorizedException(Error.INVALID_GOOGLE_TOKEN_EXCEPTION);
            }
        } catch (GeneralSecurityException e) {
            throw new UnauthorizedException(Error.INVALID_GOOGLE_TOKEN_EXCEPTION);
        } catch (IOException e) {
            throw new InternalServerErrorException(Error.INTERNAL_SERVER_ERROR);
        }

        return idToken;
    }

    public String getNameFromEmail(String email) {

        // 이메일이 null이거나 비어있으면 null을 반환
        if (email == null || email.isEmpty()) {
            return null;
        }

        // "@" 기호의 인덱스 찾기
        int atIndex = email.indexOf('@');


        if (atIndex != -1) { // "@" 기호가 존재하는 경우
            // "@" 앞부분을 substring으로 추출
            return email.substring(0, atIndex);
        } else { // "@" 기호가 없는 경우, 이메일 형식이 아니므로 null 반환
            return null;
        }
    }
}
