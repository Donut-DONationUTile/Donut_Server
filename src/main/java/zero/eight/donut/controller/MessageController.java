package zero.eight.donut.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zero.eight.donut.common.response.ApiResponse;
import zero.eight.donut.dto.SendMessageRequestDto;
import zero.eight.donut.service.MessageService;

@RequiredArgsConstructor
@RequestMapping("/api/message")
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/receiver")
    public ApiResponse<?> sendMessage(@RequestBody SendMessageRequestDto requestDto) throws FirebaseMessagingException {
        return messageService.sendMessage(requestDto);
    }
}
