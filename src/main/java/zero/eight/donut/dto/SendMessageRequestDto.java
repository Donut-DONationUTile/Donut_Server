package zero.eight.donut.dto;

import lombok.Getter;

@Getter
public class SendMessageRequestDto {
    private Long boxId; // 꾸러미 고유 ID
    private String content; // 메세지 본문
}
