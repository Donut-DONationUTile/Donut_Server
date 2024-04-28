package zero.eight.donut.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.config.jwt.JwtUtils;
import zero.eight.donut.domain.Benefit;
import zero.eight.donut.domain.Donation;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.auth.*;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.exception.UnauthorizedException;
import zero.eight.donut.repository.BenefitRepository;
import zero.eight.donut.repository.DonationRepository;
import zero.eight.donut.repository.GiverRepository;
import zero.eight.donut.repository.ReceiverRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${google.clientid}")
    private String CLIENT_ID; // @Value 어노테이션을 사용하여 값을 주입할 때, 주입 대상 필드가 static으로 선언되어 있으면 주입이 제대로 이루어지지 않을 수 있음
    private final GiverRepository giverRepository;
    private final ReceiverRepository receiverRepository;
    private final BenefitRepository benefitRepository;
    private final DonationRepository donationRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthUtils authUtils;

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
    protected void googleSignUp(String email) {
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
        
        log.info("구글 토큰 검증 함수 진입");

        GoogleIdToken idToken = null;
        try {
            idToken = GoogleIdToken.parse(new JacksonFactory(), googleToken);
            log.info("token 해체 -> {}", idToken);
        } catch (IOException e) {
            log.info("token 해체 실패");
            return null;
        }

        /*

        try {
            // 테스트
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + googleToken;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            log.info("Response Code: " + responseCode);

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            // 응답 처리
            log.info("Token Info: " + response.toString());
        }
        catch (Exception e) {
            log.info("에러");
        }
        */

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        log.info("verifier 생성 -> {}", String.valueOf(verifier));

        try {
            idToken = verifier.verify(googleToken);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }


        if (idToken != null) {

            log.info("idToken is not null");
            GoogleIdToken.Payload payload = idToken.getPayload();

//            if (!tokenVerifier(payload)) {
//                log.info("token 검증 결과 유효하지 않음");
//                return null;
//            }

            // Print user identifier
            String googleId = payload.getSubject();
            log.info("User ID: " + googleId);

            // Get profile information from payload
            boolean emailVerified = payload.getEmailVerified();

            // 불필요 정보
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

            if (!emailVerified) {
                log.info("email is not verified");
                return null;
            }

        } else {
            log.info("Token is NULL");
            // exception
            throw new UnauthorizedException(Error.INVALID_GOOGLE_TOKEN_EXCEPTION);
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
        log.info("계정 생성 함수 진입");

        // 아이디 중복 확인
        if (isDuplicatedID(requestDto.getId())) {
            return ApiResponse.failure(Error.DUPLICATED_ID);
        }
        log.info("아이디 중복 확인 완료");

        // 아이디, 비밀번호로 Receiver 객체 생성
        Receiver receiver = Receiver.builder()
                .name(requestDto.getId())
                .password(requestDto.getPassword())
                .build();
        log.info("아이디, 비밀번호로 수혜자(Receiver) 객체 생성 완료");

        // 비밀번호 암호화
        receiver.encryptPassword(bCryptPasswordEncoder);
        log.info("비밀번호 암호화 완료: encryptPassword -> {}", receiver.getPassword());

        // Receiver 객체 저장
        receiverRepository.save(receiver);
        log.info("Receiver 객체 저장 완료");

        // receiver로 benefit 생성
        Benefit benefit = createBenefit(receiver);
        log.info("benefit -> {}", benefit);
        log.info("receiver로 benefit 생성 완료");

        return ApiResponse.success(Success.SIGN_UP_SUCCESS);
    }

    public boolean isDuplicatedID(String id) {
        log.info("아이디 중복 확인 함수 진입");
        return !receiverRepository.findByName(id).isEmpty();
    }

    @Transactional
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
    protected Donation createDonation(Giver giver) {

        log.info("기부자별 기부 정보 생성 함수로 진입");
        
        Donation donation = Donation.builder()
                .giver(giver)
                .sum(0L)
                .count(0L)
                .report(0)
                .build();
        log.info("donation 객체 생성 완료");
        
        donationRepository.save(donation);
        log.info("donation 객체 저장 완료");

        return donation;
    }

    @Transactional
    protected Benefit createBenefit(Receiver receiver) {

        log.info("수혜자 정보 생성 함수로 진입");

        // 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("현재 시간 가져오기 완료 -> {}", currentTime);

        // 현재 월 가져오기
        Month currentMonth = currentTime.getMonth();
        int month = currentMonth.getValue();
        log.info("현재 월 가져오기 완료 -> {}", month);

        // 현재 연도 가져오기
        int year = currentTime.getYear();
        log.info("현재 연도 가져오기 완료 -> {}", year);

        Benefit benefit = Benefit.builder()
                .sum(0)
                .year(year)
                .month(month)
                .availability(true)
                .receiver(receiver)
                .build();
        log.info("benefit 객체 생성 완료");

        benefitRepository.save(benefit);
        log.info("benefit 객체 저장 완료");
        
        return benefit;
    }

    // 구글 토큰 자체 검증 함수
    private Boolean tokenVerifier(GoogleIdToken.Payload payload) {
        
        log.info("tokenVerifier 진입");

        // ID 토큰의 iss 클레임 값이 https://accounts.google.com 또는 accounts.google.com와 같은지 확인
        String iss = (String) payload.get("iss");
        log.info("iss -> {}", iss);
        
        if (!iss.equals("https://accounts.google.com") && !iss.equals("accounts.google.com")) {
            log.info("iss 필드 오류");
            return false;
        }
        log.info("iss 필드 검증 완료: 통과");

        // ID 토큰의 aud 클레임 값이 앱의 클라이언트 ID와 같은지 확인
        String aud = (String) payload.get("aud");
        log.info("aud -> {}", aud);
        log.info("CLIENT_ID -> {}", CLIENT_ID);

        if (!aud.equals(CLIENT_ID)) {
            log.info("aud 필드 오류");
            return false;
        }
        log.info("aud 필드 검증 완료: 통과");

        // ID 토큰의 만료 시간 (exp 클레임)이 지나지 않았는지 확인
        // 주어진 정수값 (UNIX 시간)
        long exp = (long) payload.get("exp");
        log.info("exp -> {}", exp);

        // UNIX 시간을 밀리초 단위로 변환하고, UTC 기준의 Instant로 변환
        Instant instant = Instant.ofEpochSecond(exp);

        // 현재 시간 (UTC 기준)
        Instant currentInstant = Instant.now();

        // 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        // 결과 출력
        log.info("주어진 날짜: " + formatter.format(instant));
        log.info("현재 날짜: " + formatter.format(currentInstant));

        // UTC 기준의 UNIX 시간 가져오기 (초 단위)
        long currentUnixTime = currentInstant.getEpochSecond();

        // 결과 출력
        log.info("현재 UTC 기준의 UNIX 시간: " + currentUnixTime);

        // 날짜 비교
        if (exp < currentUnixTime) {
            log.info("만료 일자가 현재보다 이전임: Expired !!!");
            return false;

        } else {
            log.info("만료 일자가 현재와 같거나 현재보다 이후임");
            log.info("exp 필드 검증 완료: 통과");
        }

        return true;
    }

    // for giver token error test
    public ApiResponse<?> getGiverEntity() {
        return ApiResponse.success(authUtils.getGiver());
    }
}
