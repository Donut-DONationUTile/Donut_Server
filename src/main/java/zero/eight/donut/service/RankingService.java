package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.Donation;
import zero.eight.donut.domain.Giver;
import zero.eight.donut.dto.auth.Role;
import zero.eight.donut.dto.ranking.RankingByNumberResponseDto;
import zero.eight.donut.dto.ranking.RankingByPriceResponseDto;
import zero.eight.donut.exception.Error;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.DonationRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankingService {

    private final DonationRepository donationRepository;
    private final AuthUtils authUtils;
    public ApiResponse<?> getRankingByPrice() {
        // 기부자인지 역할 조회
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }

        List<RankingByPriceResponseDto> rankingList = new ArrayList<>();

        // 기부 금액 내림차순으로 기부 내역 조회
        List<Donation> donationList = donationRepository.findAll(Sort.by(Sort.Direction.DESC, "sum"));
        for (int idx = 1; idx <= donationList.size(); idx++) {
            Donation d = donationList.get(idx);
            Giver giver = d.getGiver();
            if (giver == null) { // 기부자 부재 시 서버 exception error
                return ApiResponse.failure(Error.INTERNAL_SERVER_ERROR);
            }
            rankingList.add(new RankingByPriceResponseDto(idx, giver.getName(), d.getSum()));
        }

        return ApiResponse.success(Success.GET_RANKING_BY_PRICE_SUCCESS, rankingList);
    }

    public ApiResponse<?> getRankingByNumber() {
        // 기부자인지 역할 조회
        if (!authUtils.getCurrentUserRole().equals(Role.ROLE_GIVER)) {
            return ApiResponse.failure(Error.NOT_AUTHENTICATED_EXCEPTION);
        }

        List<RankingByNumberResponseDto> rankingList = new ArrayList<>();

        // 기부 횟수 내림차순으로 기부 내역 조회
        List<Donation> donationList = donationRepository.findAll(Sort.by(Sort.Direction.DESC, "count"));
        for (int idx = 1; idx <= donationList.size(); idx++) {
            Donation d = donationList.get(idx);
            Giver giver = d.getGiver();
            if (giver == null) { // 기부자 부재 시 서버 exception error
                return ApiResponse.failure(Error.INTERNAL_SERVER_ERROR);
            }
            rankingList.add(new RankingByNumberResponseDto(idx, giver.getName(), d.getCount()));
        }

        return ApiResponse.success(Success.GET_RANKING_BY_NUMBER_SUCCESS, rankingList);
    }
}
