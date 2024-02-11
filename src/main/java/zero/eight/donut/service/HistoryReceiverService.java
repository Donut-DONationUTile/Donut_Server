package zero.eight.donut.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.domain.Receiver;

@Service
@RequiredArgsConstructor
public class HistoryReceiverService {
    private final AuthUtils authUtils;
    public ApiResponse<?> receivedDonation(){
        Receiver receiver = authUtils.getReceiver();
        return ApiResponse.success();
    }
}
