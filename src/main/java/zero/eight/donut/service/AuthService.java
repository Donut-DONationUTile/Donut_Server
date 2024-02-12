package zero.eight.donut.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.JwtUtils;
import zero.eight.donut.domain.Benefit;
import zero.eight.donut.domain.Donation;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.*;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.BenefitRepository;
import zero.eight.donut.repository.DonationRepository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.Month;
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
    private final ReceiverRepository receiverRepository;
    private final BenefitRepository benefitRepository;
    private final DonationRepository donationRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public ApiResponse<?> googleSignIn(String idToken) {

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
                    log.info("신규 가입 유저");
                    googleSignUp(email);
                }
        );


        giver = giverRepository.findByEmail(email);

        MemberDto member = MemberDto.builder()
                .name(giver.get().getEmail())
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
        AuthResponseDto loginResponse = AuthResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(member))
                .refreshtoken(jwtUtils.createRefreshToken(member))
                .name(member.getName())
                .build();

        return ApiResponse.success(Success.SIGN_IN_SUCCESS, loginResponse);
    }

    @Transactional
    private void googleSignUp(String email) {
        String name = getNameFromEmail(email);
        Giver giver = Giver.builder()
                .name(name)
                .email(email)
                // createdAt, updatedAt
                .build();

        giverRepository.save(giver);
        
        // 최초 가입 시 Donation 객체도 생성
        createDonation(giver);
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

    @Transactional
    public ApiResponse<?> createAccount(AuthRequestDto requestDto) {

        // 아이디 중복 확인
        if (isDuplicatedID(requestDto.getId())) {
            return ApiResponse.failure(Error.DUPLICATED_ID);
        }

        // 아이디, 비밀번호로 Receiver 객체 생성
        Receiver receiver = Receiver.builder()
                .name(requestDto.getId())
                .password(requestDto.getPassword())
                .build();

        // 비밀번호 암호화
        receiver.encryptPassword(bCryptPasswordEncoder);

        // Receiver 객체 저장
        receiverRepository.save(receiver);

        // receiver로 benefit 생성
        Benefit benefit = createBenefit(receiver);
        log.info("benefit -> {}", benefit);

        return ApiResponse.success(Success.SIGN_UP_SUCCESS);
    }

    public boolean isDuplicatedID(String id) {
        return !receiverRepository.findByName(id).isEmpty();
    }

    public ApiResponse<?> receiverSignIn(AuthRequestDto requestDto) {

        Optional<Receiver> receiver = receiverRepository.findByName(requestDto.getId());

        // 존재하지 않는 계정일 경우 error
        if (receiver.isEmpty()) {
            log.info("존재하지 않는 아이디");
            return ApiResponse.failure(Error.INVALID_ID_PASSWORD_EXCEPTION);
        }

        // 비밀번호가 일치하지 않을 경우 error
        if (!receiver.get().verifyPassword(requestDto.getPassword(), bCryptPasswordEncoder)) {
            return ApiResponse.failure(Error.INVALID_ID_PASSWORD_EXCEPTION);
        }

        // 사용자 아이디 & 역할로 jwt 생성
        MemberDto member = MemberDto.builder()
                .name(receiver.get().getName())
                .role(Role.ROLE_RECEIVER)
                .build();
        AuthResponseDto loginResponse = AuthResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(member))
                .refreshtoken(jwtUtils.createRefreshToken(member))
                .name(member.getName())
                .build();

        return ApiResponse.success(Success.SIGN_IN_SUCCESS, loginResponse);
    }

    @Transactional
    public ApiResponse<?> createGiverToken(AuthTestDto authTestDto) {

        String email = authTestDto.getEmail();

        log.info("Input test email -> {}", email);

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
                    log.info("신규 가입 유저");
                    googleSignUp(email);
                }
        );


        giver = giverRepository.findByEmail(email);

        MemberDto member = MemberDto.builder()
                .name(giver.get().getEmail())
                .role(Role.ROLE_GIVER)
                .build();

        // 사용자 이메일 & 역할로 jwt 생성
        AuthResponseDto loginResponse = AuthResponseDto.builder()
                .accesstoken(jwtUtils.createAccessToken(member))
                .refreshtoken(jwtUtils.createRefreshToken(member))
                .name(member.getName())
                .build();

        return ApiResponse.success(Success.SIGN_IN_SUCCESS, loginResponse);
    }

    @Transactional
    private Donation createDonation(Giver giver) {
        Donation donation = Donation.builder()
                .giver(giver)
                .sum(0L)
                .count(0L)
                .report(0)
                .build();

        donationRepository.save(donation);

        return donation;
    }

    @Transactional
    private Benefit createBenefit(Receiver receiver) {

        // 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();

        // 현재 월 가져오기
        Month currentMonth = currentTime.getMonth();
        int month = currentMonth.getValue();

        // 현재 연도 가져오기
        int year = currentTime.getYear();

        Benefit benefit = Benefit.builder()
                .sum(0)
                .year(year)
                .month(month)
                .availability(true)
                .receiver(receiver)
                .build();

        benefitRepository.save(benefit);
        return benefit;
    }
}
