package zero.eight.donut.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.config.jwt.AuthUtils;
import zero.eight.donut.domain.Gift;
import zero.eight.donut.domain.Giftbox;
import zero.eight.donut.domain.Message;
import zero.eight.donut.domain.Receiver;
import zero.eight.donut.dto.SendMessageRequestDto;
import zero.eight.donut.exception.Success;
import zero.eight.donut.repository.GiftRepository;
import zero.eight.donut.repository.GiftboxRepository;
import zero.eight.donut.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final GiftboxRepository giftboxRepository;

    private final AuthUtils authUtils;
    private final MessageRepository messageRepository;
    private final GiftRepository giftRepository;

    public MessageService(GiftboxRepository giftboxRepository, AuthUtils authUtils, MessageRepository messageRepository, GiftRepository giftRepository) {
        this.giftboxRepository = giftboxRepository;
        this.authUtils = authUtils;
        this.messageRepository = messageRepository;
        this.giftRepository= giftRepository;
    }

    @Transactional
    public ApiResponse<?> sendMessage(SendMessageRequestDto requestDto) {
        // 수혜자
        Receiver receiver = authUtils.getReceiver();

        // 꾸러미 ID로 꾸러미에 속한 기프티콘 리스트 찾기
//        Giftbox box = giftboxRepository.findById(requestDto.getBoxId()).orElseThrow();
//        List<Gift> giftList = box.get기ftList();

        // 기프티콘마다 신규 메세지 등록 여부(idMsgReceived)를 true로 변경하기
        // 메세지 객체 생성 및 저장하기(기프티콘 고유 ID, 기부자 고유 ID, 수혜자 고유 ID, requestDto.content)
//        for (Gift g : giftList) {
//            g.updateIsMsgReceived();
//            Message message = Message.builder()
//                    .content(requestDto.getContent())
//                    .receiver(receiver)
//                    .gift(g)
//                    .giver(g.getGiver())
//                    .build();
//            messageRepository.save(message);
//        }
        //기프티콘 찾
        Gift gift = giftRepository.findById(requestDto.getGiftId()).orElseThrow();
        gift.updateIsMsgReceived();
        Message message = Message.builder()
                    .content(requestDto.getContent())
                    .receiver(receiver)
                    .gift(gift)
                    .giver(gift.getGiver())
                    .build();
            messageRepository.save(message);
        /////////////////////////////////////
        /////////////////////////////////////
        /////////////////////////////////////
        // 기부자에게 알림 보내기
        /////////////////////////////////////
        /////////////////////////////////////
        /////////////////////////////////////


        // 반환하기
        return ApiResponse.success(Success.SEND_MESSAGE_SUCCESS);
    }
}
